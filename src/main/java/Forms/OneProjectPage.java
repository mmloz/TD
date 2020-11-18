package Forms;

import aquality.selenium.elements.interfaces.ITextBox;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class OneProjectPage extends FormWithFactory {
    private static final By locator = By.xpath("//div[@class='panel-heading']");
    private static final String name = "Project page";

    private static final String testTablePath = "//table[@class='table']//tr";
    private static final String columnDataPath = testTablePath + "/td[%s]";
    private static final String columnDataName = "Column: ";

    private static final String testByNamePath = ("//a[contains(text(), '%s')]");
    private static final String testByNameTitle = "Test name";

    public OneProjectPage() {
        super(locator, name);
    }

    private List<String> getTestColumn(String column){
        String columnPathTemplate = String.format(columnDataPath, column);
        List<ITextBox> elements = elementFactory.findElements(By.xpath(columnPathTemplate), columnDataName + column, ITextBox.class);
        List<String> data = new ArrayList<>();

        for (ITextBox current : elements){
            data.add(current.getText());
        }

        return data;
    }

    public List<String> getTestNames(){
        return getTestColumn("1");
    }

    public List<String> getTestDates(){
        return getTestColumn("4");
    }

    public ITextBox getTest(String name){
        String path = String.format(testByNamePath, name);
        By xPath = By.xpath(path);
        return elementFactory.getTextBox(xPath, testByNameTitle);
    }

    public int getTestId(String testName){
        ITextBox test = getTest(testName);
        return Integer.parseInt(test.getAttribute("href").split("testId=")[1]);
    }
}
