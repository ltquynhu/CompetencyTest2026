package pageObjects.login;

import org.openqa.selenium.WebDriver;
import core.BasePage;
import pageUIs.login.LoginPageUI;

public class LoginPageObject extends BasePage {

    public LoginPageObject(WebDriver driver) {
        super(driver);
    }

    public void enterEmail(String email) {
        sendKeyToElement(LoginPageUI.EMAIL_INPUT, email);
    }

    public void enterPassword(String password) {
        sendKeyToElement(LoginPageUI.PASSWORD_INPUT, password);

    }

    public void clickSignIn() {
        clickToElement(LoginPageUI.SUBMIT_BUTTON);
    }

}
