package Forms;

import aquality.selenium.elements.interfaces.ITextBox;
import org.openqa.selenium.By;

import java.util.ArrayList;
import java.util.List;

public class OneProjectPage extends FormWithFactory {
    private static final By locator = By.xpath("//div[@class='panel-heading']");
    private static final String name = "Project page";

    private static final String columnsPath = "//table[@class='table']//tr//th";
    private static final String columnPath = "//table[@class='table']//tr/td[%s]";

    private static final String columnsName = "Columns in test table";
    private static final String columnName = "Column: ";

    private static final String testByNamePath = ("//a[contains(text(), '%s')]");
    private static final String testByNameTitle = "Test name";

    private static final String testIdUrlParameter = "testId=";

    public OneProjectPage() {
        super(locator, name);
    }

    private int getColumnNum(String name){
        List<ITextBox> elements = elementFactory.findElements(By.xpath(columnsPath), columnsName, ITextBox.class);
        for (int i = 0; i < elements.size(); i++){
            if (elements.get(i).getText().contains(name)) return i + 1;
        }

        throw new IllegalArgumentException("No such column found");
    }

    public List<String> getColumnText(String columnName){
        int column = getColumnNum(columnName);

        String columnPathTemplate = String.format(columnPath, column);
        List<ITextBox> elements = elementFactory.findElements(By.xpath(columnPathTemplate), OneProjectPage.columnName + columnName, ITextBox.class);
        List<String> data = new ArrayList<>();

        for (ITextBox current : elements){
            data.add(current.getText());
        }

        return data;
    }

    public ITextBox getTest(String name){
        By xPath = By.xpath(String.format(testByNamePath, name));
        return elementFactory.getTextBox(xPath, testByNameTitle);
    }

    public int getTestId(String testName){
        ITextBox test = getTest(testName);
        return Integer.parseInt(test.getAttribute("href").split(testIdUrlParameter)[1]);
    }
}
