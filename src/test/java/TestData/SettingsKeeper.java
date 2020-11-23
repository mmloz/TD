package TestData;

import TestData.Constants.JsonKeys;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;

import static TestData.Constants.Filenames.settingsFilename;

public class SettingsKeeper {
    private final ISettingsFile settings;

    public SettingsKeeper(){
        settings = new JsonSettingsFile(settingsFilename);
    }

    public String getHomeUrl(){
        return settings.getValue(JsonKeys.urlKey).toString();
    }
}