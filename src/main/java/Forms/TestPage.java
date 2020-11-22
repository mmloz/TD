package Forms;

import aquality.selenium.elements.interfaces.ITextBox;
import org.openqa.selenium.By;

public class TestPage extends FormWithFactory{
    private static final By locator = By.xpath("//ol[@class='breadcrumb']");
    private static final String name = "Test page";

    private static final By logsTextPath = By.xpath("//div[text()='Logs']/following-sibling::table[1]");
    private static final String logsTextName = "Log field";

    private static final By attachmentPath = By.xpath("//div[text()='Attachments']/following-sibling::table[1]");
    private static final String attachmentName = "Attachment";

    private static final By imagePath = By.xpath("//div[text()='Attachments']/following-sibling::table[1]//a//img");
    private static final String imageName = "Attachment image";


    public TestPage() {
        super(locator, name);
    }

    public String getLogsText(){
        ITextBox logs = elementFactory.getTextBox(logsTextPath, logsTextName);
        return logs.getText();
    }

    public String getImageSrc(){
        ITextBox attachments = elementFactory.getTextBox(imagePath, imageName);
        return attachments.getAttribute("src").split(",")[1];
    }

    public String getAttachmentType(){
        ITextBox imageType = elementFactory.getTextBox(attachmentPath, attachmentName);
        return imageType.getText();
    }
}
