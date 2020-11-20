package Tests;

import Api.TestRail.TestRailRequests;
import Api.TestRail.TestRailUtils;
import Api.UnionReporting.Constants.UnionUrlParams;
import Api.UnionReporting.UnionApiRequests;
import Forms.OneProjectPage;
import Forms.ProjectsPage;
import Forms.TestPage;
import TestData.SettingsKeeper;
import TestData.TestDataKeeper;
import Utils.*;
import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/*
    TODO
        * ожидания
        * сообщения в ассертах
 */

public class TestCase {
    private Browser browser;
    private TestDataKeeper testData;
    private SettingsKeeper settings;
    private UnionApiRequests unionApi;
    private UrlBuilder urlBuilder;

    @BeforeTest
    public void setUp(){
        //browser = AqualityServices.getBrowser();

        testData = new TestDataKeeper();
        settings = new SettingsKeeper();
        unionApi = new UnionApiRequests();
        urlBuilder = new UrlBuilder();
    }

    @Test
    public void testCase() {
        // Запросом к апи получить токен согласно номеру варианта
        String token = unionApi.getToken(testData.getVariant());

        // Токен сгенерирован
        Assert.assertNotNull(token);
        Assert.assertEquals(token.length(), testData.getTokenSize());

        // Перейти на сайт. Пройти необходимую авторизацию.
        String authUrl = urlBuilder.getAuthUrl(testData.getLogin(), testData.getPassword());
        browser.goTo(authUrl);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // проверка что открылась страница
        ProjectsPage projectsPage = new ProjectsPage();
        Assert.assertTrue(projectsPage.home.state().isDisplayed());

        // С помощью cookie передать сгенерированный на шаге 1 токен (параметр token)
        Cookie cookie = new Cookie(UnionUrlParams.TOKEN, token);
        browser.getDriver().manage().addCookie(cookie);

        // Обновить страницу
        browser.refresh();
        browser.waitForPageToLoad();

        // Открылась страница проектов. После обновления страницы, в футере указан верный номер варианта
        Assert.assertTrue(projectsPage.getFooterText()
                .contains(String.format(testData.getFooterText(), testData.getVariant())));

        // Перейти на страницу проекта Nexage
        OneProjectPage nexagePage = new OneProjectPage();
        projectsPage.clickProject(testData.getProjectId());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Запросом к апи получить список тестов в JSON-формате
        List<String> namesFromApi = JsonUtils.getTestName(unionApi.getJsonTests(testData.getProjectId()));

        // Тесты, находящиеся на первой странице отсортированы по убыванию даты
        List<String> testDates = nexagePage.getTestDates();
        Assert.assertTrue(DateUtils.isDateSortedByDesc(testDates));

        // и соответствуют тем, которые вернул запрос к апи
        List<String> namesFromUI = nexagePage.getTestNames();
        Assert.assertTrue(namesFromApi.containsAll(namesFromUI));

        // Вернуться на предыдущую страницу в браузере(страница проектов).
        browser.goBack();
        browser.getDriver().manage().deleteAllCookies();
        browser.refresh();
        browser.waitForPageToLoad();

        // Нажать на +Add. Ввести название проекта, и сохранить.
        String projName = RandomStringUtils.randomAlphanumeric(10);
        String reqMsg = String.format(testData.getSuccessMsg(), projName);

        projectsPage.clickAdd();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        projectsPage.modalForm.enterText(projName);
        projectsPage.modalForm.save();

        // После сохранения проекта появилось сообщение об успешном сохранении.
        String message = projectsPage.modalForm.getMessage();
        Assert.assertEquals(message, reqMsg);

        // Для закрытия окна добавления проекта вызвать js-метод closePopUp().
        projectsPage.modalForm.close();
        // После вызова метода окно добавления проекта закрылось.

        // Обновить страницу
        browser.refresh();
        browser.waitForPageToLoad();

        // После обновления страницы проект появился в списке
        Assert.assertTrue(projectsPage.isProjectExists(projName));

        // Перейти на страницу созданного проекта. Добавить тест через БД(вместе с логом и скриншотом текущей страницы).
        int projectId = projectsPage.getProjId(projName);
        projectsPage.clickOnProject(projectId);

        OneProjectPage testProjPage = new OneProjectPage();

        // Добавить тест через БД
        String testName = "Test name new";
        int statusId = 1;
        String methodName = "Method name";
        int sessionId = 1;
        String environment = "Environment";
        String browserName = browser.getBrowserName().toString();

        DBConnector db = new DBConnector();
        db.insertTest(testName, statusId, methodName, projectId, sessionId, environment, browserName);

        // Тест отобразился без обновления страницы
        Assert.assertTrue(testProjPage.getTest(testName).state().waitForDisplayed());

        // Добавить лог и скриншот
        int testId = testProjPage.getTestId(testName);
        byte[] screen = AqualityServices.getBrowser().getScreenshot();
        ImageUtils.saveScreenshot(screen, browser.getDownloadDirectory(), testData.getScreenName());
        String logText = "Example log";

        db.insertLogInTest(logText, 0, testId);
        db.insertScreenInTest(ImageUtils.bytesToHex(screen), testId);

        // Перейти на страницу созданного теста. Проверить корректность информации.
        testProjPage.getTest(testName).click();
        TestPage test = new TestPage();

        // Все поля имеют верное значение
        Assert.assertEquals(logText, test.getLogsText());
        Assert.assertTrue(test.getAttachmentType().contains("image/png"));
        Assert.assertNotNull(test.getImageSrc());

        // Скриншот соответствует отправленному
        byte[] decodedImg = Base64.getDecoder()
                .decode(test.getImageSrc().getBytes(StandardCharsets.UTF_8));
        FileUtils.writeByteToImage(browser.getDownloadDirectory(), testData.getImageName(), decodedImg);

        String screenPath = String.format("%s//%s", browser.getDownloadDirectory(), testData.getScreenName());
        String attachmentPath = String.format("%s//%s", browser.getDownloadDirectory(), testData.getImageName());
        Assert.assertTrue(ImageUtils.compareImages(screenPath, attachmentPath));


        //////////////////////////////////////////////////////////
        // TestRail

        String testRailUrl = testData.getTRUrl();
        String testRailLogin = testData.getTRLogin();
        String testRailPassword = testData.getTRPassword();
        TestRailRequests testRailRequests = new TestRailRequests(testRailUrl, testRailLogin, testRailPassword);

        // получаем айди сьюита
        String getSuiteResponse = testRailRequests.getSuites(testData.getTRProject());
        String suiteId = JsonUtils.getId(getSuiteResponse, 0);

        // создаем ран
        String runParams = TestRailUtils.createRun(testData.getTRRunName(), suiteId, testData.getTRCaseId());
        String postRunResponse = testRailRequests.postRun(testData.getTRProject(), runParams);
        String runId = JsonUtils.getId(postRunResponse);

        // получаем айди теста в ране
        String getTestIdResponse = testRailRequests.getTestIdInRun(runId);
        String trTestId = JsonUtils.getId(getTestIdResponse, 0);

        // выставляем результаты теста
        String testResultParams = TestRailUtils.createTestResult(testData.getTRCaseStatus());
        String addResultResponse = testRailRequests.addResults(trTestId, testResultParams);
        String resultId = JsonUtils.getId(addResultResponse);

        // добавляем скрин
        testRailRequests.addScreenToResult(resultId, screenPath);

    }

    @Test
    public void checkBrowser(){
        System.setProperty("webdriver.gecko.driver", "src/test/resources/downloads/geckodriver");

        FirefoxBinary firefoxBinary = new FirefoxBinary();
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary(firefoxBinary);
        options.setHeadless(true);  // <-- headless set here
        WebDriver driver = new FirefoxDriver(options);
        driver.get("https://www.google.com");
    }
}
