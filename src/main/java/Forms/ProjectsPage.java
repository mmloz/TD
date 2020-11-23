package Forms;

import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ITextBox;
import org.openqa.selenium.By;

import java.util.List;

public class ProjectsPage extends FormWithFactory {
    private static final By locator = By.xpath("//div[@class='container main-container']");
    private static final String name = "Projects page";

    private static final By homeBtnPath = By.xpath("//a[@href='/web/projects']");
    private static final String homeBtnName = "Home button";

    private static final By footerTextPath = By.xpath("//p[@class='text-muted text-center footer-text']");
    private static final String footerTextName = "Footer text";

    private static final By addBtnPath = By.xpath("//button[@data-target='#addProject']");
    private static final String addBtnName = "Add button";

    private static final String projectPathTemplate = "//a[contains(text(), '%s')]";
    private static final String projectName = "Project";

    private static final String projectIdUrlParameter = "projectId=";

    public IButton home = elementFactory.getButton(homeBtnPath, homeBtnName);
    public ModalForm modalForm = new ModalForm();

    public ProjectsPage() {
        super(locator, name);
    }

    public String getFooterText(){
        ITextBox footer = elementFactory.getTextBox(footerTextPath, footerTextName);
        return footer.getText();
    }

    public void clickAdd(){
        IButton add = elementFactory.getButton(addBtnPath, addBtnName);
        add.click();
    }

    public boolean isProjectExists(String name){
        List<ITextBox> projs = elementFactory.findElements(getProjectPath(name), projectName, ITextBox.class);
        return projs.size() > 0;
    }

    public ITextBox getProject(String name){
        return elementFactory.getTextBox(getProjectPath(name), projectName);
    }

    private String getProjUrl(String name){
        List<ITextBox> projs = elementFactory.findElements(getProjectPath(name), projectName, ITextBox.class);
        return projs.get(0).getAttribute("href");
    }

    public int getProjId(String name){
        return Integer.parseInt(getProjUrl(name).split(projectIdUrlParameter)[1]);
    }

    public void clickOnProject(String name){
        ITextBox project = elementFactory.getTextBox(getProjectPath(name), projectName);
        project.click();
    }

    private By getProjectPath(String text){
        String temp = String.format(projectPathTemplate, text);
        return By.xpath(temp);
    }
}
