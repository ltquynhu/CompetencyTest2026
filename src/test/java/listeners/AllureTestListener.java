package listeners;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import base.BaseTest;
import io.qameta.allure.Attachment;

public class AllureTestListener implements ITestListener {

	private void flushCollectedFailures(ITestResult iTestResult) {
		Object testClass = iTestResult.getInstance();
		if (testClass instanceof BaseTest) {
			try {
				((BaseTest) testClass).throwAllCollectedFailures(iTestResult);
			} catch (AssertionError softFailureError) {
				if (iTestResult.getThrowable() != null && iTestResult.getThrowable() != softFailureError) {
					softFailureError.addSuppressed(iTestResult.getThrowable());
				}
				iTestResult.setThrowable(softFailureError);
				iTestResult.setStatus(ITestResult.FAILURE);
			}
		}
	}

	// Screenshot attachments for Allure
	@Attachment(type = "image/png")
	public static byte[] saveScreenshotPNG(String testName, WebDriver driver) {
		return (byte[]) ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
	}

	// Text attachments for Allure
	@Attachment(type = "text/plain")
	public static String saveTextLog(String message) {
		return message;
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		Object testClass = iTestResult.getInstance();
		if (testClass instanceof BaseTest) {
			WebDriver driver = ((BaseTest) testClass).getDriver();
			if (driver != null) {
				saveTextLog("FAILED TEST: " + iTestResult.getName());
			}
		}

		flushCollectedFailures(iTestResult);
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		flushCollectedFailures(iTestResult);
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		flushCollectedFailures(iTestResult);
	}

}