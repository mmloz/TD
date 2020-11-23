package Forms;

import aquality.selenium.browser.AqualityServices;
import aquality.selenium.elements.interfaces.IElementFactory;
import aquality.selenium.forms.Form;
import org.openqa.selenium.By;


public abstract class FormWithFactory extends Form {
    protected IElementFactory elementFactory = AqualityServices.getElementFactory();

    protected FormWithFactory(By locator, String name) {
        super(locator, name);
    }
}
