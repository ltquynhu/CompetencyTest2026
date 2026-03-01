package config;

public class FrameworkConstants {

    public static final String OS_NAME = System.getProperty("os.name").toLowerCase();
    public static final String PROJECT_PATH = System.getProperty("user.dir");

    public static final String ALLURE_REPORT_PATH = PROJECT_PATH + "/target";
    public static final String ENVIRONMENT_PROJECT_PATH = PROJECT_PATH + "/src/test/resources/environmentConfig";

    public static final String UPLOAD_FILES_PATH = PROJECT_PATH + "/src/test/resources/uploadFiles";
    public static final long SHORT_TIMEOUT = 0;
    public static final long LONG_TIMEOUT = 30;
}
