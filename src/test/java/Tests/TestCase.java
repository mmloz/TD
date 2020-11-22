package Tests;

import Api.TestRail.TestRailRequests;
import Api.TestRail.TestRailUtils;
import Api.UnionReporting.Constants.UnionUrlParams;
import Api.UnionReporting.UnionApiRequests;
import Api.UnionReporting.UrlBuilder;
import Constants.AssertMessages;
import Forms.OneProjectPage;
import Forms.ProjectsPage;
import Forms.TestPage;
import TestData.AuthKeeper;
import TestData.SettingsKeeper;
import TestData.TestDataKeeper;
import Utils.DataBaseUtils.DBConnector;
import Utils.DateUtils;
import Utils.FileUtils;
import Utils.ImageUtils;
import Utils.JsonUtils;
import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class TestCase {
    private Browser browser;
    private TestDataKeeper testData;
    private SettingsKeeper settings;
    private UnionApiRequests unionApi;
    private UrlBuilder urlBuilder;
    private AuthKeeper auth;

    @BeforeTest
    public void setUp(){
        browser = AqualityServices.getBrowser();

        testData = new TestDataKeeper();
        settings = new SettingsKeeper();

        unionApi = new UnionApiRequests();
        urlBuilder = new UrlBuilder();

        auth = new AuthKeeper();
    }


    @Test
    public void unionReportingTest() {
        // Запросом к апи получить токен согласно номеру варианта
        String token = unionApi.getToken(testData.getVariant());

        // Токен сгенерирован
        Assert.assertNotNull(token, AssertMessages.nullToken);
        Assert.assertEquals(token.length(), testData.getTokenSize(), AssertMessages.invalidTokenSize);

        // Перейти на сайт. Пройти необходимую авторизацию
        String authUrl = urlBuilder.getAuthUrl(auth.getUnionLogin(), auth.getUnionPassword());
        browser.goTo(authUrl);
        browser.waitForPageToLoad();

        // Открылась страница проектов
        ProjectsPage projectsPage = new ProjectsPage();
        Assert.assertTrue(projectsPage.home.state().isDisplayed(), AssertMessages.projectsPageError);

        // С помощью cookie передать сгенерированный на шаге 1 токен (параметр token)
        Cookie cookie = new Cookie(UnionUrlParams.TOKEN, token);
        browser.getDriver().manage().addCookie(cookie);

        // Обновить страницу
        browser.refresh();
        browser.waitForPageToLoad();

        // После обновления страницы, в футере указан верный номер варианта
        Assert.assertTrue(projectsPage.getFooterText()
                .contains(String.format(testData.getFooterText(), testData.getVariant())),
                AssertMessages.footerTextError);

        // Перейти на страницу проекта Nexage
        OneProjectPage nexagePage = new OneProjectPage();
        projectsPage.clickOnProject(testData.getProjectName());
        browser.waitForPageToLoad();

        // Запросом к апи получить список тестов в JSON-формате
        List<String> namesFromApi = JsonUtils.getTestName(unionApi.getJsonTests(testData.getProjectId()));

        // Тесты, находящиеся на первой странице отсортированы по убыванию даты
        List<String> testDates = nexagePage.getColumnText(testData.getTestDateColumnName());
        Assert.assertTrue(DateUtils.isDateSortedByDesc(testDates), AssertMessages.testOrderError);

        // и соответствуют тем, которые вернул запрос к апи
        List<String> namesFromUI = nexagePage.getColumnText(testData.getTestNameColumnName());
        Assert.assertTrue(namesFromApi.containsAll(namesFromUI), AssertMessages.uiAndApiComparisonError);

        // Вернуться на предыдущую страницу в браузере (страница проектов)
        browser.goBack();
        browser.getDriver().manage().deleteAllCookies();
        browser.refresh();
        browser.waitForPageToLoad();

        // Нажать на +Add. Ввести название проекта, и сохранить
        String projName = RandomStringUtils.randomAlphanumeric(testData.getStringLength());
        String reqMsg = String.format(testData.getSuccessMsg(), projName);

        projectsPage.clickAdd();

        projectsPage.modalForm.enterText(projName);
        projectsPage.modalForm.save();

        // После сохранения проекта появилось сообщение об успешном сохранении
        String message = projectsPage.modalForm.getMessage();
        Assert.assertEquals(message, reqMsg, AssertMessages.addProjectTextError);

        // Для закрытия окна добавления проекта вызвать js-метод closePopUp()
        projectsPage.modalForm.close();

        // После вызова метода окно добавления проекта закрылось
        projectsPage.modalForm.state().waitForNotDisplayed();
        Assert.assertFalse(projectsPage.modalForm.state().isDisplayed(), AssertMessages.modalFormCloseError);

        // Обновить страницу
        browser.refresh();
        browser.waitForPageToLoad();

        // После обновления страницы проект появился в списке
        Assert.assertTrue(projectsPage.isProjectExists(projName), AssertMessages.projectCreationError);

        // Перейти на страницу созданного проекта.
        int projectId = projectsPage.getProjId(projName);
        projectsPage.clickOnProject(projName);

        OneProjectPage testProjPage = new OneProjectPage();

        // Добавить тест через БД (вместе с логом и скриншотом текущей страницы)
        String testName = RandomStringUtils.randomAlphanumeric(testData.getStringLength());
        int statusId = testData.getStatusId();
        String methodName = RandomStringUtils.randomAlphanumeric(testData.getStringLength());
        int sessionId = testData.getSessionId();
        String environment = RandomStringUtils.randomAlphanumeric(testData.getStringLength());
        String browserName = browser.getBrowserName().toString();

        DBConnector db = new DBConnector();
        db.insertTest(testName, statusId, methodName, projectId, sessionId, environment, browserName);

        // Тест отобразился без обновления страницы
        Assert.assertTrue(testProjPage.getTest(testName).state().waitForDisplayed(), AssertMessages.testCreationError);

        // Добавить лог и скриншот
        int testId = testProjPage.getTestId(testName);
        byte[] screen = AqualityServices.getBrowser().getScreenshot();
        ImageUtils.saveScreenshot(screen, browser.getDownloadDirectory(), testData.getScreenName(), testData.getScreenFormat());
        String logText = RandomStringUtils.randomAlphanumeric(testData.getStringLength());;

        db.insertLogInTest(logText, testId);
        db.insertScreenInTest(ImageUtils.bytesToHex(screen), testData.getAttachmentType(), testId);

        // Перейти на страницу созданного теста. Проверить корректность информации
        testProjPage.getTest(testName).click();
        TestPage test = new TestPage();

        // Все поля имеют верное значение
        Assert.assertEquals(logText, test.getLogsText(), AssertMessages.logTextError);
        Assert.assertTrue(test.getAttachmentType().contains(testData.getAttachmentType()), AssertMessages.attachmentTypeError);
        Assert.assertNotNull(test.getImageSrc(), AssertMessages.nullAttachmentSrc);

        // Скриншот соответствует отправленному
        byte[] decodedImg = Base64.getDecoder()
                .decode(test.getImageSrc().getBytes(StandardCharsets.UTF_8));
        FileUtils.writeByteToImage(browser.getDownloadDirectory(), testData.getImageName(), decodedImg);

        String screenPath = FileUtils.getPath(browser.getDownloadDirectory(), testData.getScreenName());
        String attachmentPath = FileUtils.getPath(browser.getDownloadDirectory(), testData.getImageName());
        Assert.assertTrue(ImageUtils.compareImages(screenPath, attachmentPath), AssertMessages.attachmentNotTheSameError);
    }

    @AfterTest
    public void tearDown(){
        // Настроить интеграцию проекта с TestRail(выставление результата теста, добавление скриншота)
        String testRailUrl = testData.getTRUrl();
        String testRailLogin = auth.getTrLogin();
        String testRailPassword = auth.getTrPassword();
        TestRailRequests testRailRequests = new TestRailRequests(testRailUrl, testRailLogin, testRailPassword);

        // получаем айди сьюита
        int firstFoundPos = 0;
        String getSuiteResponse = testRailRequests.getSuites(testData.getTRProject());
        String suiteId = JsonUtils.getId(getSuiteResponse, firstFoundPos);

        // создаем ран
        String runParams = TestRailUtils.createRun(testData.getTRRunName(), suiteId, testData.getTRCaseId());
        String postRunResponse = testRailRequests.postRun(testData.getTRProject(), runParams);
        String runId = JsonUtils.getId(postRunResponse);

        // получаем айди теста в ране
        String getTestIdResponse = testRailRequests.getTestIdInRun(runId);
        String trTestId = JsonUtils.getId(getTestIdResponse, firstFoundPos);

        // выставляем результаты теста
        String testResultParams = TestRailUtils.createTestResult(testData.getTRCaseStatus());
        String addResultResponse = testRailRequests.addResults(trTestId, testResultParams);
        String resultId = JsonUtils.getId(addResultResponse);

        // добавляем скрин
        testRailRequests.addScreenToResult(resultId, FileUtils.getPath(browser.getDownloadDirectory(), testData.getScreenName()));
    }
}
