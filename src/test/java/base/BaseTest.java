package base;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import config.FrameworkConstants;
import config.OwnerConfig;
import core.PageGeneratorManager;
import io.qameta.allure.Allure;
import listeners.AllureTestListener;
import pageObjects.login.LoginPageObject;

public class BaseTest {
    public static Logger log;
    protected static WebDriver driver;
    protected static OwnerConfig readConfig;

    /**
     * Returns the current WebDriver for this test session.
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * Initializes the logger for the current class.
     */
    public BaseTest() {
        log = LogManager.getLogger(getClass());

    }

    /**
     * Hook that runs before the entire suite.
     */
    @BeforeSuite
    protected void beforeAll() {

    }

    /**
     * Test setup: initialize browser and authenticate user.
     */
    @Parameters("server")
    @BeforeTest
    protected static void setUp(String server) {
        initBrowser(server);
        ensureLoginSession();

    }

    /**
     * Restores session if available; otherwise logs in via UI and saves session cookies.
     */
    protected static void ensureLoginSession() {
        String userKey = readConfig.email();
        boolean restored = SessionManager.restoreSession(driver, readConfig.appUrl(), userKey);

        if (restored && !isLoginPage()) {
            Allure.step("Restored login session from cookie fixture for user: " + userKey);
            return;
        }

        Allure.step("Login session not available. Performing UI login.");
        driver.get(readConfig.appUrl());
        loginToLinkedIn();
        SessionManager.saveSession(driver, userKey);
        Allure.step("Saved login session cookies for user: " + userKey);
    }

    /**
     * Checks whether the current page requires login.
     */
    protected static boolean isLoginPage() {
        String currentUrl = driver.getCurrentUrl().toLowerCase();
        if (currentUrl.contains("login")) {
            return true;
        }
        return !driver.findElements(By.cssSelector("#session_key")).isEmpty();
    }

    /**
     * Performs LinkedIn login using credentials from configuration.
     */
    protected static void loginToLinkedIn() {
        LoginPageObject loginPage = PageGeneratorManager.getLoginPageObject(driver);
        loginPage.enterEmail(readConfig.email());
        loginPage.enterPassword(readConfig.password());
        loginPage.clickSignIn();
    }

    /**
     * Hook for test-level teardown.
     */
    @AfterTest(alwaysRun = true)
    protected void tearDownTest() {
        // Reserved for test-level teardown
    }

    /**
     * Final suite teardown: closes browser and cleans up driver processes.
     */
    @AfterSuite(alwaysRun = true)
    protected static void tearDown() {
        close();
        SessionManager.clearAll();
    }

    /**
     * Initializes browser once per test session and opens the app URL.
     */
    protected synchronized static WebDriver initBrowser(String server) {
        if (driver == null) {
            String browser = DriverFactory.resolveBrowser();
            String mobileDevice = MobileEmulationManager.resolveMobileDevice();
            try {
                Allure.step("Using browser: " + browser);

                if (mobileDevice != null && !mobileDevice.isBlank()) {
                    Allure.step("Mobile emulation device: " + mobileDevice);
                    if (!MobileEmulationManager.isSupported(mobileDevice)) {
                        Allure.step("Unsupported MOBILE_DEVICE: " + mobileDevice + ". "
                                + MobileEmulationManager.supportedDevicesMessage());
                    }
                }

                driver = DriverFactory.createDriver(browser, mobileDevice);
            } finally {
                Runtime.getRuntime().addShutdownHook(new Thread(new BrowserCleanup()));
            }

            driver.manage().deleteAllCookies();

            readConfig = readOwnerProperties(server);
            driver.get(readConfig.appUrl());

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(FrameworkConstants.SHORT_TIMEOUT));
            Allure.step(
                    "---------------------------------------- Started the browser ----------------------------------------");
        }
        return driver;
    }

    /**
     * Closes current browser and kills driver process based on OS.
     */
    protected static void close() {
        String cmd = null;
        try {
            String osName = FrameworkConstants.OS_NAME;
            Allure.step("OS name = " + osName);

            String driverInstanceName = driver.toString().toLowerCase();
            Allure.step("Driver instance name = " + driverInstanceName);

            // Get a browser driver name
            String browserDriverName = null;
            if (driverInstanceName.contains("chrome")) {
                browserDriverName = "chromedriver";
            } else if (driverInstanceName.contains("internetexplorer")) {
                browserDriverName = "IEDriverServer";
            } else if (driverInstanceName.contains("firefox")) {
                browserDriverName = "geckodriver";
            } else if (driverInstanceName.contains("edge")) {
                browserDriverName = "msedgedriver";
            } else if (driverInstanceName.contains("opera")) {
                browserDriverName = "operadriver";
            } else {
                browserDriverName = "safaridriver";
            }

            // Create a cmd for each OS with browser driver name
            if (osName.contains("window")) {
                cmd = "taskkill /F /FI \"IMAGENAME eq " + browserDriverName + "*\"";
            } else {
                cmd = "pkill " + browserDriverName;
            }

            // Check if driver is equal null or not. If not, delete all cookie and quit
            // driver
            if (driver != null) {
                driver.manage().deleteAllCookies();
                driver.quit();
                // Another way: initBrowser().quit();

                Allure.step("------------- Closed the browser -------------");
            }
        } catch (Exception e) {
            Allure.step("Cannot close the browser");
            Allure.step(e.getMessage());
        } finally {
            // Quit drive in Task Manager

            try {
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private static class BrowserCleanup implements Runnable {
        /**
         * Placeholder for shutdown-hook cleanup extension.
         */
        @Override
        public void run() {
            // close();
        }
    }

    /**
     * Verifies condition is true; records soft failure if assertion fails.
     */
    public boolean verifyTrue(boolean condition, String checkName) {
        boolean pass = true;
        try {
            Assert.assertTrue(condition);
            Allure.step(checkName + ": PASSED");
        } catch (Throwable e) {
            pass = handleVerifyFailed(checkName, e);
        }
        return pass;
    }

    /**
     * Verifies condition is false; records soft failure if assertion fails.
     */
    public boolean verifyFalse(boolean condition, String checkName) {
        boolean pass = true;
        try {
            Assert.assertFalse(condition);
            Allure.step(checkName + ": PASSED");
        } catch (Throwable e) {
            pass = handleVerifyFailed(checkName, e);
        }
        return pass;
    }

    /**
     * Verifies actual equals expected; records soft failure if assertion fails.
     */
    public boolean verifyEquals(Object actual, Object expected, String checkName) {
        boolean pass = true;
        try {
            Assert.assertEquals(actual, expected);
            Allure.step(checkName + ": PASSED");
        } catch (Throwable e) {
            pass = handleVerifyFailed(checkName, e);
        }
        return pass;
    }

    /**
     * Shared assertion-failure handler: stores failure, logs details, and captures screenshot.
     */
    private boolean handleVerifyFailed(String checkName, Throwable e) {
        VerificationFailures.getFailures().addFailureForTest(Reporter.getCurrentTestResult(), e);
        Reporter.getCurrentTestResult().setThrowable(e);
        AllureTestListener.saveTextLog("ASSERT FAILED: " + checkName + " | " + e.getMessage());
        try {
            Allure.step(checkName + ": FAILED", () -> {
                AllureTestListener.saveScreenshotPNG("", driver);
                throw new AssertionError(e.getMessage());
            });
        } catch (AssertionError ignore) {
        }
        return false;
    }

    /**
     * Reads environment configuration based on the server parameter.
     */
    private static OwnerConfig readOwnerProperties(String server) {
        ConfigFactory.setProperty("env", server + "Environment");
        return ConfigFactory.create(OwnerConfig.class);
    }

    /**
     * Runs each case block inside a test and aggregates failures per case.
     */
    protected void runCaseByCase(String caseName, Runnable steps) {
        ITestResult currentResult = Reporter.getCurrentTestResult();
        int failuresBefore = getFailureCount(currentResult);

        try {
            Allure.step(caseName, () -> {
                try {
                    steps.run();
                    if (hasNewFailures(currentResult, failuresBefore)) {
                        throw createCaseCheckFailure();
                    }
                } catch (Throwable throwable) {
                    collectCaseFailure(currentResult, caseName, throwable);
                    throw throwable;
                }
            });
        } catch (Throwable ignore) {
            // Case failure is already collected inside the parent step.
        }
    }

    /**
     * Checks whether new failures were added after running a case.
     */
    private boolean hasNewFailures(ITestResult result, int failuresBefore) {
        int failuresAfter = getFailureCount(result);
        return failuresAfter > failuresBefore;
    }

    /**
     * Creates a standard failure when a case contains verification failures.
     */
    private AssertionError createCaseCheckFailure() {
        return new AssertionError("One or more checks failed");
    }

    /**
     * Adds a case failure to the soft-failure collection.
     */
    public void collectCaseFailure(ITestResult result, String caseName, Throwable throwable) {
        if (throwable == null) {
            return;
        }
        AssertionError caseError = new AssertionError(
                "Case execution failed: " + caseName + " | " + throwable.getMessage(), throwable);
        VerificationFailures.getFailures().addFailureForTest(result, caseError);
        if (result != null) {
            result.setThrowable(caseError);
            result.setStatus(ITestResult.FAILURE);
        }
    }

    /**
     * Returns number of collected failures for current test result.
     */
    private int getFailureCount(ITestResult result) {
        if (result == null) {
            return 0;
        }
        return VerificationFailures.getFailures().getFailuresForTest(result).size();
    }

    /**
     * Throws all collected soft failures for the current test.
     */
    public void throwAllCollectedFailures(ITestResult result) {
        if (result == null) {
            return;
        }
        List<ITestResult> keysToRemove = new ArrayList<>();
        List<Throwable> failures = getFailuresForCurrentTest(result, keysToRemove);
        removeCollectedFailures(keysToRemove);

        if (!failures.isEmpty()) {
            throwFailures(failures);
        }
    }

    /**
     * Gets failures related to current test and marks keys for cleanup.
     */
    private List<Throwable> getFailuresForCurrentTest(ITestResult currentResult, List<ITestResult> keysToRemove) {
        List<Throwable> failures = new ArrayList<>();

        for (Map.Entry<ITestResult, List<Throwable>> entry : VerificationFailures.getFailures().entrySet()) {
            ITestResult storedResult = entry.getKey();
            if (storedResult == null || isSameTestResult(storedResult, currentResult)) {
                failures.addAll(entry.getValue());
                keysToRemove.add(storedResult);
            }
        }
        return failures;
    }

    /**
     * Removes processed failures from temporary storage.
     */
    private void removeCollectedFailures(List<ITestResult> keysToRemove) {
        for (ITestResult key : keysToRemove) {
            VerificationFailures.getFailures().remove(key);
        }
    }

    /**
     * Throws aggregated failures: direct throw for one failure, combined message for many.
     */
    private void throwFailures(List<Throwable> failures) {
        if (failures.size() == 1) {
            Throwable failure = failures.get(0);
            throw new AssertionError("Assertion error: " + failure.getMessage(), failure);
        }

        throw new AssertionError("Multiple assertion failures:\n" + buildFailureMessage(failures));
    }

    /**
     * Builds detailed message for multiple failures.
     */
    private String buildFailureMessage(List<Throwable> failures) {
        StringBuilder failureMessage = new StringBuilder();
        for (int i = 0; i < failures.size(); i++) {
            failureMessage.append(i + 1)
                    .append(". ")
                    .append(failures.get(i).getLocalizedMessage())
                    .append("\n");
        }
        return failureMessage.toString();
    }

    /**
     * Checks whether two ITestResult objects reference the same test method and instance.
     */
    private boolean isSameTestResult(ITestResult storedResult, ITestResult currentResult) {
        if (storedResult == null || currentResult == null) {
            return false;
        }
        if (storedResult == currentResult) {
            return true;
        }

        boolean sameInstance = storedResult.getInstance() == currentResult.getInstance();
        boolean sameMethod = storedResult.getMethod() != null
                && currentResult.getMethod() != null
                && storedResult.getMethod().getQualifiedName().equals(currentResult.getMethod().getQualifiedName());

        return sameInstance && sameMethod;
    }

}