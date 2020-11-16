package Utils;

import TestData.SettingsKeeper;

public class UrlBuilder {
    private static final SettingsKeeper settings = new SettingsKeeper();

    public String getTokenUrl(){
        return settings.getHomeUrl() + "/api/token/get";
    }
}
