package Utils;

import TestData.SettingsKeeper;

public class UrlBuilder {
    private static final SettingsKeeper settings = new SettingsKeeper();

    public String getTokenUrl(){
        return settings.getHomeUrl() + "/api/token/get";
    }

    public String getAuthUrl(String login, String password){
        String[] parse = settings.getHomeUrl().split("//");
        String newUrl = parse[0] + "//" + "%s:%s@" + parse[1];
        return String.format(newUrl, login, password) + "/web";
    }

    public String getTestsListJsonUrl(){
        return settings.getHomeUrl() + "/api/test/get/json";
    }
}
