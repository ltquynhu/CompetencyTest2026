package pageUIs;

public class BaseElementUI {
    public static final String DYNAMIC_INPUT_BY_NAME_FIELD = "xpath=//label[text()='%s']//following-sibling::input";
    public static final String DYNAMIC_SELECT_DROPDOWN_BY_NAME_FIELD = "xpath=//span[text()='%s']//ancestor::label//following-sibling::select";
    public static final String DYNAMIC_WARNING_MESSAGE_BY_INPUT_NAME_FIELD = "xpath=//label[text()='%s']//ancestor::div[contains(@class,'artdeco-text-input')]//following-sibling::div//span[@class='artdeco-inline-feedback__message']";
    public static final String DYNAMIC_WARNING_MESSAGE_BY_DROPDOWN_NAME_FIELD = "xpath=//span[text()='%s']//ancestor::label//following-sibling::div//span";

    
    public static final String LOANGDING_ICON = "xpath=//div[contains(@class,'artdeco-loader')]";
    public static final String INITIAL_LOAD_ANIMATION = "css=.initial-load-animation";
}


