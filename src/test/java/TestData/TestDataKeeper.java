package TestData;

import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;

import static TestData.Constants.Filenames.dataFilename;
import static TestData.Constants.JsonKeys.*;

public class TestDataKeeper {
    private final ISettingsFile testData;

    public TestDataKeeper(){
        testData = new JsonSettingsFile(dataFilename);
    }

    public String getVariant(){
        return testData.getValue(varKey).toString();
    }

    public int getTokenSize(){
        return Integer.parseInt(testData.getValue(tokenSizeKey).toString());
    }
}
