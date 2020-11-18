package Forms;

import aquality.selenium.browser.AqualityServices;
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

    private static final String projectXPath = "//a[@href='allTests?projectId=%s']";
    private static final String projectName = "Project";

    private static final By addBtnPath = By.xpath("//button[@data-target='#addProject']");
    private static final String addBtnName = "Add button";

    public IButton home = elementFactory.getButton(homeBtnPath, homeBtnName);
    public ModalForm modalForm;

    public ProjectsPage() {
        super(locator, name);
    }

    public String getFooterText(){
        ITextBox footer = elementFactory.getTextBox(footerTextPath, footerTextName);
        return footer.getText();
    }

    public void clickProject(String projectId){
        String currentProjectXPath = String.format(projectXPath, projectId);
        IButton project = elementFactory.getButton(By.xpath(currentProjectXPath), projectName);

        project.click();
    }

    public void clickAdd(){
        IButton add = elementFactory.getButton(addBtnPath, addBtnName);
        add.click();
        modalForm = new ModalForm();
    }

    public boolean isProjectExists(String name){
        String temp = String.format("//a[contains(text(), '%s')]", name);
        By path = By.xpath(temp);
        List<ITextBox> projs = elementFactory.findElements(path, projectName, ITextBox.class);
        return projs.size() > 0;
    }

    private String getProjUrl(String name){
        String temp = String.format("//a[contains(text(), '%s')]", name);
        By path = By.xpath(temp);
        List<ITextBox> projs = elementFactory.findElements(path, "project", ITextBox.class);
        return projs.get(0).getAttribute("href");
    }

    public int getProjId(String name){
        return Integer.parseInt(getProjUrl(name).split("projectId=")[1]);
    }

    public void clickOnProject(int projectId){
        AqualityServices.getBrowser().goTo("http://localhost:8080/web/allTests?projectId=" + projectId);
    }
}
