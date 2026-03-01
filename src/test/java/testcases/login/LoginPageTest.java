package testcases.login;

import org.testng.annotations.Test;

import base.BaseTest;

public class LoginPageTest extends BaseTest {

    @Test(description = "Login to Linkedin")
    public static void loginSuccessfully() {
        loginToLinkedIn();
    }
}
