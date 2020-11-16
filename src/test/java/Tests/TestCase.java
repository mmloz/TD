package Tests;

import Utils.RequestUtils;
import Utils.UrlBuilder;
import aquality.selenium.browser.AqualityServices;
import aquality.selenium.browser.Browser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TestCase {
    private Browser browser;
    private UrlBuilder urlBuilder;

    @BeforeTest
    public void setUp(){
        browser = AqualityServices.getBrowser();
        urlBuilder = new UrlBuilder();
    }

    @Test
    public void testCase() {
        //  Запросом к апи получить токен согласно номеру варианта
        RequestUtils requestUtils = new RequestUtils();

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("variant", "1"));

        String token = null;
        try {
            token = requestUtils.postWithParams(urlBuilder.getTokenUrl(), params);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(token);
    }
}
