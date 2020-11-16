package Tests;

import Api.UnionApiRequests;
import TestData.TestDataKeeper;
import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;


public class TestCase {
    private Browser browser;
    private TestDataKeeper testData;
    private UnionApiRequests unionApi;

    @BeforeTest
    public void setUp(){
        browser = AqualityServices.getBrowser();

        testData = new TestDataKeeper();
        unionApi = new UnionApiRequests();
    }

    @Test
    public void testCase() {
        // Запросом к апи получить токен согласно номеру варианта
        String token = unionApi.getToken(testData.getVariant());

        // Токен сгенерирован
        Assert.assertNotNull(token);
        Assert.assertEquals(token.length(), testData.getTokenSize());
    }
}
