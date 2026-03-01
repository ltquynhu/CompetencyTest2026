package pageObjects;

import org.openqa.selenium.WebDriver;

import core.BasePage;
import pageUIs.BaseElementUI;

public class BaseElementPageObject extends BasePage {

    public BaseElementPageObject(WebDriver driver) {
        super(driver);
        //TODO Auto-generated constructor stub
    }

    public boolean waitingForLinkedinAnimationInvisible() {
        return waitForElementInvisible(BaseElementUI.INITIAL_LOAD_ANIMATION);
    }

    public boolean waitingForLoadingIconInvisible() {
        return waitForElementInvisible(BaseElementUI.LOANGDING_ICON);
    }
    
}
