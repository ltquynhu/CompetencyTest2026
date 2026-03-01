package pageObjects.jobs;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import pageObjects.BaseElementPageObject;
import pageUIs.jobs.SearchJobsPageUI;

public class SearchJobsPageObject extends BaseElementPageObject {

    public SearchJobsPageObject(WebDriver driver) {
        super(driver);
    }

    @Step("Click on Easy Apply filter button")
    public void clickEasyApplyFiltersButton() {
        if (isElementUndisplayed(SearchJobsPageUI.EASY_APPLY_FILTERS_BUTTON)) {
            clickToElement(SearchJobsPageUI.ALL_FILTERS_BUTTON);
            turnOnToggle(SearchJobsPageUI.EASY_APPLY_TOGGLE);
            clickToElement(SearchJobsPageUI.SHOW_RESULT_BUTTON);

        } else {
            clickToElement(SearchJobsPageUI.EASY_APPLY_FILTERS_BUTTON);
        }

    }

    @Step("Click on Easy Apply button")
    public void clickEasyButton() {
        waitingForLoadingIconInvisible();
        clickToElement(SearchJobsPageUI.EASY_APPLY_BUTTON);
    }

    @Step("Verify that the application form is open")
    public boolean isApplicationOpened() {
        return isElementDisplayed(SearchJobsPageUI.DIALOG_HEADER);
    }

    public List<String> getRequiredTextInputFields() {
        List<String> requiredFieldList = new ArrayList<>();
        for (WebElement ele : getListWebElements(SearchJobsPageUI.DIALOG_NUMBER_OF_REQUIRED_TEXT_INPUT_FIELDS)) {
            requiredFieldList.add(ele.getText());
        }
        return requiredFieldList;
    }

    @Step("Verify that each input field is displayed")
    public boolean isInputTextFieldDisplayed() {
        List<String> requiredFieldList = getRequiredTextInputFields();
        for (String fieldName : requiredFieldList) {
            if (isElementDisplayed(SearchJobsPageUI.DYNAMIC_INPUT_BY_NAME_FIELD, fieldName) == false) {
                return false;
            }
        }
        return true;
    }

    public List<String> getRequiredSelectFields() {
        List<String> requiredFieldList = new ArrayList<>();
        for (WebElement ele : getListWebElements(SearchJobsPageUI.DIALOG_NUMBER_OF_REQUIRED_SELECT_FIELDS)) {
            requiredFieldList.add(ele.getText());
        }
        return requiredFieldList;
    }

    @Step("Verify that each required dropdown field is displayed")
    public boolean isDropdownListFieldDisplayed() {
        List<String> requiredFieldList = getRequiredSelectFields();
        for (String fieldName : requiredFieldList) {
            if (isElementDisplayed(SearchJobsPageUI.DIALOG_NUMBER_OF_REQUIRED_SELECT_FIELDS, fieldName) == false) {
                return false;
            }
        }
        return true;
    }

    @Step("Click Next")
    public void clickNextButton() {
        clickToElement(SearchJobsPageUI.DIALOG_NEXT_BUTTON);
    }

    public boolean isUploadResumeDisplayed() {
        if (isElementUndisplayed(SearchJobsPageUI.DIALOG_UPLOAD_RESUME_BUTTON)) {
            clickNextButton();
        }
        return isElementDisplayed(SearchJobsPageUI.DIALOG_UPLOAD_RESUME_BUTTON);
    }

    @Step("Clear all text fields")
    public void clearAllTextFields() {
        List<String> requiredFieldList = getRequiredTextInputFields();
        System.out.println("requiredFieldList" + requiredFieldList);
        for (String fieldName : requiredFieldList) {
            Allure.step("Clear text in input field: " + fieldName);
            clearText(SearchJobsPageUI.DYNAMIC_INPUT_BY_NAME_FIELD, fieldName);
        }

    }

    @Step("Select the first option in each dropdown")
    public void selectNoOption() {
        List<String> requiredFieldList = getRequiredSelectFields();
        for (String fieldName : requiredFieldList) {
            Allure.step("Select first option in dropdown field: " + fieldName);
            selectFirstOptionInDropDownList(SearchJobsPageUI.DYNAMIC_SELECT_DROPDOWN_BY_NAME_FIELD, fieldName);
        }
    }

    public void clickDismissButton() {
        clickToElement(SearchJobsPageUI.DIALOG_DISMISS_BUTTON);
    }

    public void clickDiscardButton() {
        clickToElement(SearchJobsPageUI.DIALOG_DISCARD_BUTTON);
    }

    public void closeApplicationDialogIfPresent() {
        if (!isElementUndisplayed(SearchJobsPageUI.DIALOG_DISMISS_BUTTON)) {
            clickDismissButton();
            if (!isElementUndisplayed(SearchJobsPageUI.DIALOG_DISCARD_BUTTON)) {
                clickDiscardButton();
            }
        }
    }

    public boolean isWarningMessageDisplayedForTextInput() {
        List<String> requiredFieldList = getRequiredTextInputFields();
        for (String fieldName : requiredFieldList) {
            Allure.step("Verify that an error message is shown for input field: " + fieldName);
            if (isElementDisplayed(SearchJobsPageUI.DYNAMIC_WARNING_MESSAGE_BY_INPUT_NAME_FIELD, fieldName) == false) {
                return false;

            }
        }
        return true;
    }

    @Step("Verify error message is shown")
    public boolean isWarningMessageDisplayedUForDropdownField() {
        List<String> requiredFieldList = getRequiredSelectFields();
        for (String fieldName : requiredFieldList) {
            Allure.step("Dropdown field: " + fieldName);
            if (isElementDisplayed(SearchJobsPageUI.DYNAMIC_WARNING_MESSAGE_BY_DROPDOWN_NAME_FIELD, fieldName) == false) {
                return false;
            }

        }
        return true;
    }

    public void uploadResumeFile(String filePath) {
        uploadFileToElement(SearchJobsPageUI.DIALOG_UPLOAD_RESUME_INPUT, filePath);
        waitingForLoadingIconInvisible();
    }

    public boolean isUploadedResumeDisplayed(String filePath, boolean valid) {
        String fileName = Path.of(filePath).getFileName().toString();
        if (valid) {
            return isElementDisplayed(SearchJobsPageUI.DYNAMIC_DIALOG_UPLOADED_FILE_BY_NAME, fileName)
                    && isElementUndisplayed(SearchJobsPageUI.DIALOG_DOWNLOAD_RESUME_DISABLED_ICON);
        }
        return isElementDisplayed(SearchJobsPageUI.DYNAMIC_DIALOG_UPLOADED_FILE_BY_NAME, fileName)
                && isElementDisplayed(SearchJobsPageUI.DIALOG_DOWNLOAD_RESUME_DISABLED_ICON);
    }

    public boolean isWarningrMessageDisplayed() {
        return isElementDisplayed(SearchJobsPageUI.ERROR_MESSAGE_BY_UPLOAD_RESUME);
    }

    public boolean isWarningMessageUndisplayed() {
        return isElementUndisplayed(SearchJobsPageUI.ERROR_MESSAGE_BY_UPLOAD_RESUME);
    }

    public boolean isErrorMessageOfInvalidFileUnDisplayed() {
        return isElementUndisplayed(SearchJobsPageUI.ERROR_MESSAGE_BY_UPLOAD_INVALID_FILE);
    }

    public boolean isErrorMessageOfInvalidFileDisplayed() {
        return isElementDisplayed(SearchJobsPageUI.ERROR_MESSAGE_BY_UPLOAD_INVALID_FILE);
    }

    public String getErrorMessageWithLargeFile() {
        return getText(SearchJobsPageUI.ERROR_MESSAGE_BY_UPLOAD_INVALID_FILE);
    }



}
