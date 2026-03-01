package pageUIs.jobs;

import pageUIs.BaseElementUI;

public class SearchJobsPageUI extends BaseElementUI {
    // public static final String SEARCH_NOW_BUTTON = "xpath=//span[text()='Search
    // now']//ancestor::a";
    public static final String SEARCH_INPUT = "xpath=//input[@data-testid='typeahead-input']";

    public static final String EASY_APPLY_FILTERS_BUTTON = "xpath=//button[text()='Easy Apply']";
    public static final String ALL_FILTERS_BUTTON = "xpath=//button[text()='All filters']";
    public static final String EASY_APPLY_TOGGLE = "xpath=//div[@role='dialog']//h3[contains(.,'Easy Apply')]//following-sibling::div//input[@role='switch']";
    public static final String SHOW_RESULT_BUTTON = "xpath=//span[text()='Reset']//ancestor::button//following-sibling::button";
    public static final String EASY_APPLY_BUTTON = "xpath=//div[@class='display-flex']//button[@id='jobs-apply-button-id']";

    // APPLICATION FORM
    public static final String DIALOG_HEADER = "xpath=//div[@role='dialog']//h2[contains(.,'Apply to')]";

    public static final String DIALOG_NUMBER_OF_REQUIRED_TEXT_INPUT_FIELDS = "xpath=//input[@required]//preceding-sibling::label";
    public static final String DIALOG_NUMBER_OF_REQUIRED_SELECT_FIELDS = "xpath=//select[@required]//preceding-sibling::label/span[@aria-hidden='true']";

    public static final String DIALOG_BACK_BUTTON = "xpath=//button/span[text()='Back']";

    public static final String DIALOG_NEXT_BUTTON = "xpath=//footer[@role='presentation']//span[@class='artdeco-button__text' and normalize-space()!='Back']";

    public static final String DIALOG_UPLOAD_RESUME_BUTTON = "xpath=//span[text()='Upload resume']";

    public static final String DIALOG_UPLOAD_RESUME_INPUT = "xpath=//span[text()='Upload resume']//parent::label//following-sibling::input[@name='file']";

    public static final String DYNAMIC_DIALOG_UPLOADED_FILE_BY_NAME = "xpath=//h3[contains(@class,'jobs-document-upload') and text()='%s']";

    public static final String DIALOG_DISMISS_BUTTON = "xpath=//div[@role='dialog']//button[@aria-label='Dismiss']";

    public static final String DIALOG_DISCARD_BUTTON = "xpath=//button/span[text()='Discard']";

    public static final String ERROR_MESSAGE_BY_UPLOAD_RESUME = "xpath=//span[text()='Upload resume']//parent::label//following-sibling::div//span[@class='artdeco-inline-feedback__message']";

    public static final String ERROR_MESSAGE_BY_UPLOAD_INVALID_FILE = "xpath=//span[contains(@class,'artdeco-inline-feedback__message')]/span";

    public static final String DIALOG_DOWNLOAD_RESUME_DISABLED_ICON = "xpath=//button[contains(@aria-label,'Download resume') and @disabled]";

}
