package testcases.jobs;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import base.BaseTest;
import core.PageGeneratorManager;
import pageObjects.jobs.JobsPageObject;
import pageObjects.jobs.SearchJobsPageObject;
import testdata.UploadCase;
import testdata.UploadFileDataProvider;

public class EasyApplyTest extends BaseTest {
    JobsPageObject jobsPage;
    SearchJobsPageObject searchJobs;

    @BeforeClass
    public void beforeClass() {
        jobsPage = PageGeneratorManager.getJobsPageObject(driver);
        searchJobs = jobsPage.searchAJob("Automation Test");
        searchJobs.clickEasyApplyFiltersButton();
    }

    @BeforeMethod
    public void beforeMethod() {
        searchJobs.closeApplicationDialogIfPresent();
        searchJobs.clickEasyButton();
        verifyTrue(searchJobs.isApplicationOpened(), "Application form is open");
    }

    // @Test(description = "Open application form successfully", priority = 1)
    public void id0001_openApplicationFormSuccessfully() {

        verifyTrue(searchJobs.isInputTextFieldDisplayed(), "All required text input fields are displayed");
        verifyTrue(searchJobs.isDropdownListFieldDisplayed(), "All required dropdown fields are displayed");
        verifyTrue(searchJobs.isUploadResumeDisplayed(), "Upload Resume section is displayed");
    }

    // @Test(description = "Verify required field enforcement : Input & Dropdown fields",priority = 2)
    public void id0002_verifyRequiredFieldEnforcement() {
        searchJobs.clearAllTextFields();
        verifyTrue(searchJobs.isWarningMessageDisplayedForTextInput(),
                "Validation messages are displayed for required text input fields");

        searchJobs.selectNoOption();
        verifyTrue(searchJobs.isWarningMessageDisplayedUForDropdownField(),
                "Validation messages are displayed for required dropdown fields");

    }

    // @Test(description = "Verify required field enforcement : Upload resume",priority = 3)
    public void id0003_verifyRequiredFieldEnforcement() {
        verifyTrue(searchJobs.isUploadResumeDisplayed(), "Upload Resume section is displayed");
        searchJobs.clickNextButton();
        verifyTrue(searchJobs.isWarningrMessageDisplayed(),
                "Warning message is displayed for the Upload Resume field");
    }

    @Test(description = "File upload field behavior", priority = 4)
    public void id0003_verifyFileUpLoadBehavior() {

        verifyTrue(searchJobs.isUploadResumeDisplayed(), "Upload Resume section is displayed");
        for (UploadCase uploadCase : UploadFileDataProvider.uploadCases()) {
            runCaseByCase("File upload field behavior: " + uploadCase.getCaseName(),
                    () -> verifyUploadCase(uploadCase));
        }
    }

    private void verifyUploadCase(UploadCase uploadCase) {
        String filePath = uploadCase.getFilePath();
        searchJobs.uploadResumeFile(filePath);
        verifyTrue(searchJobs.isWarningMessageUndisplayed(), "Warning message is not displayed");
        if (uploadCase.isValid()) {
            verifyTrue(searchJobs.isErrorMessageOfInvalidFileUnDisplayed(),
                    "Error message is not displayed");
            verifyTrue(searchJobs.isUploadedResumeDisplayed(filePath, true), uploadCase.getCaseName());
        } else {
            verifyTrue(searchJobs.isErrorMessageOfInvalidFileDisplayed(),
                    "Error message is displayed ");
            verifyTrue(searchJobs.isUploadedResumeDisplayed(filePath, false), uploadCase.getCaseName());
            verifyEquals(searchJobs.getErrorMessageWithLargeFile(),uploadCase.getExpectedMessage(),"Error Message displays correctly");
        }

    }
}