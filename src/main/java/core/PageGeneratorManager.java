package core;

import org.openqa.selenium.WebDriver;

import pageObjects.jobs.JobsPageObject;
import pageObjects.jobs.SearchJobsPageObject;
import pageObjects.login.LoginPageObject;

public class PageGeneratorManager {

    WebDriver driver;

   public static LoginPageObject getLoginPageObject(WebDriver driver){
    return new LoginPageObject(driver);
   }

   public static JobsPageObject getJobsPageObject(WebDriver driver) {
    return new JobsPageObject(driver);
   }

   public static SearchJobsPageObject getSearchJobsPageObject(WebDriver driver) {
    return new SearchJobsPageObject(driver);
   }


}
