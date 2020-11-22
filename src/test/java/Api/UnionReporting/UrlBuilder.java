package Api.UnionReporting;

import Api.UnionReporting.Constants.UnionApiUrls;
import TestData.SettingsKeeper;

public class UrlBuilder {
    private static final SettingsKeeper settings = new SettingsKeeper();

    public String getTokenUrl(){
        return settings.getHomeUrl() + UnionApiUrls.GET_TOKEN;
    }

    public String getAuthUrl(String login, String password){
        String[] parse = settings.getHomeUrl().split("//");
        String newUrl = parse[0] + "//" + "%s:%s@" + parse[1];
        return String.format(newUrl, login, password) + UnionApiUrls.WEB;
    }

    public String getTestsListJsonUrl(){
        return settings.getHomeUrl() + UnionApiUrls.GET_TESTS_JSON;
    }
}
