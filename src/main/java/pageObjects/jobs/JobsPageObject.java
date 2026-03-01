package pageObjects.jobs;

import org.openqa.selenium.WebDriver;
import core.PageGeneratorManager;
import io.qameta.allure.Step;
import pageObjects.BaseElementPageObject;
import pageUIs.jobs.JobsPageUI;

public class JobsPageObject extends BaseElementPageObject {

    public JobsPageObject(WebDriver driver) {
        super(driver);
    }

    @Step("Search a job with name: : {0}")
    public SearchJobsPageObject searchAJob(String name) {
        sendKeyToElement(JobsPageUI.SEARCH_INPUT, name);
        pressEnterKey(JobsPageUI.SEARCH_INPUT);
        waitingForLinkedinAnimationInvisible();
        return PageGeneratorManager.getSearchJobsPageObject(driver);
    }
}
