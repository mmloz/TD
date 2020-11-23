package Forms;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

public class ModalForm extends FormWithFactory {
    private static final By locator = By.xpath("//div[@class='modal-dialog']");
    private static final String name = "Modal dialog";

    private static final By inputPath = By.xpath("//input[@id='projectName']");
    private static final String inputName = "Input project name";

    private static final By saveBtnPath = By.xpath("//button[@class='btn btn-primary']");
    private static final String saveBtnName = "Save project";

    private static final By successMsgPath = By.xpath("//div[@class='alert alert-success']");
    private static final String successMsgName = "Success message";

    public ModalForm() {
        super(locator, name);
    }

    public void enterText(String text){
        ITextBox textInput = elementFactory.getTextBox(inputPath, inputName);
        textInput.sendKeys(text);
    }

    public void save(){
        IButton save = elementFactory.getButton(saveBtnPath, saveBtnName);
        save.getJsActions().click();
    }

    public String getMessage(){
        ITextBox message = elementFactory.getTextBox(successMsgPath, successMsgName);
        return message.getText();
    }

    public void close(){
        JavascriptExecutor executor = (JavascriptExecutor) AqualityServices.getBrowser().getDriver();
        executor.executeScript("$('.modal.in').modal('hide')");
    }
}