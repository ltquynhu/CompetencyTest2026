package core;

import java.time.Duration;
import java.util.List;
import java.util.NoSuchElementException;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import config.FrameworkConstants;

public class BasePage {
    long longTimeout = FrameworkConstants.LONG_TIMEOUT;
    long shortTimeout = FrameworkConstants.SHORT_TIMEOUT;

    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    private By getByLocator(String locatorType) {
        By by = null;
        if (locatorType.toLowerCase().startsWith("id=")) {
            by = By.id(locatorType.substring(3));
        } else if (locatorType.toLowerCase().startsWith("class=")) {
            by = By.className(locatorType.substring(
                    6));
        } else if (locatorType.toLowerCase().startsWith("name")) {
            by = By.name(locatorType.substring(5));
        } else if (locatorType.toLowerCase().startsWith("css=")) {
            by = By.cssSelector(locatorType.substring(4));
        } else if (locatorType.toLowerCase().startsWith("xpath=")) {
            by = By.xpath(locatorType.substring(6));
        } else {
            throw new RuntimeException("Locator type is not support");
        }
        return by;
    }

    private void setImplicitWait(long timeout) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
    }

    /* WebDriverWait */
    protected void waitForElementVisible(String locator) {
        // locator = String.format(locator);
        new WebDriverWait(driver, Duration.ofSeconds(longTimeout))
                .until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }

    protected WebElement waitForElementVisible(String locator,
            String... restParams) {
        locator = String.format(locator, (Object[]) restParams);
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator)));
    }



    protected boolean waitForElementInvisible(String locator) {
        // locator = String.format(locator);
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }

    protected boolean waitForElementInvisible(String locator, String... restParams) {
        locator = String.format(locator, (Object[]) restParams);
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions.invisibilityOfElementLocated(getByLocator(locator)));
    }

    protected WebElement waitForElementClickable(String locator) {
        locator = String.format(locator);
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
    }

    protected WebElement waitForElementClickable(String locator, String... restParams) {
        locator = String.format(locator, (Object[]) restParams);
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions.elementToBeClickable(getByLocator(locator)));
    }

    /* Web Element */
    protected WebElement getWebElement(String locator) {
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions
                        .refreshed(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator))));
    }

    protected WebElement getWebElement(String locator,
            String... restParams) {
        locator = String.format(locator, (Object[]) restParams);
        return (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions
                        .refreshed(ExpectedConditions.visibilityOfElementLocated(getByLocator(locator))));
    }

    protected List<WebElement> getListWebElements(String locator) {
        return driver.findElements(getByLocator(locator));
    }

    protected List<WebElement> getListWebElements(String locator, String... restParams) {
        locator = String.format(locator, (Object[]) restParams);
        waitForElementVisible(locator);
        return driver.findElements(getByLocator(locator));
    }

    protected void clickToElement(String locator) {
        try {
            waitForElementClickable(locator).click();
        } catch (Exception e) {
            waitForElementClickable(locator).click();
        }
    }

    protected void clickToElement(String locator, String... restParams) {

        try {
            waitForElementClickable(locator, restParams).click();
        } catch (Exception e) {
            waitForElementClickable(locator).click();
        }
    }

    protected void clearText(String locator, String... restParams) {
        try {
            waitForElementVisible(locator, restParams).clear();
        } catch (StaleElementReferenceException e) {
            waitForElementVisible(locator, restParams).clear();
        }
    }

    protected void sendKeyToElement(String locator, String value, String... restParams) {
        clearText(locator, restParams);
        getWebElement(locator, restParams).sendKeys(value);
    }

    protected void uploadFileToElement(String locator, String filePath) {
        WebElement ele = (new WebDriverWait(driver, Duration.ofSeconds(longTimeout)))
                .until(ExpectedConditions
                        .refreshed(ExpectedConditions.presenceOfElementLocated(getByLocator(locator))));
        ele.sendKeys(filePath);
    }

    protected void pressEnterKey(String locator) {
        getWebElement(locator).sendKeys(Keys.ENTER);
    }

    protected String getText(String locator) {
        return getWebElement(locator).getText();
    }

    protected String getText(String locator, String... restParams) {
        return getWebElement(locator, restParams).getText();
    }

    protected String getAttribute(String locator, String name) {
        return getWebElement(locator).getAttribute(name);
    }

    protected String getAttribute(String locator, String name, String... restParams) {
        return getWebElement(locator, restParams).getAttribute(name);
    }

    protected String getAttributeText(String locator) {
        return getAttribute(locator, "value");
    }

    protected String getAttributeText(String locator, String... restParams) {
        return getAttribute(locator, "value", restParams);
    }

    protected boolean isElementDisplayed(String locator) {
        try {
            return getWebElement(locator).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }
    }

    protected boolean isElementDisplayed(String locator, String... restParams) {
        try {
            return getWebElement(locator, restParams).isDisplayed();
        } catch (TimeoutException | NoSuchElementException e) {
            return false;
        }

    }

    protected boolean isElementUndisplayed(String locator) {
        setImplicitWait(5);
        List<WebElement> elements = getListWebElements(locator);
        setImplicitWait(shortTimeout);

        // No element in DOM
        if (elements.size() == 0) {
            return true;
            // Invisible
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }

    }

    protected boolean isElementSelected(String locator) {
        return getWebElement(locator).isSelected();

    }

    protected boolean isElementEnable(String locator) {
        return getWebElement(locator).isEnabled();
    }

    protected void turnOnToggle(String locator) {
        String valueCheck = getWebElement(locator).getAttribute("aria-checked");
        if (!valueCheck.equals("false")) {
            clickToElement(locator);
        }
    }

    protected void selectFirstOptionInDropDownList(String locString, String... restParams) {
        try {
            Select sl = new Select(getWebElement(locString, restParams));
            if (!sl.getOptions().isEmpty()) {
                sl.selectByIndex(0);
            }
        } catch (StaleElementReferenceException e) {
            Select sl = new Select(getWebElement(locString, restParams));
            if (!sl.getOptions().isEmpty()) {
                sl.selectByIndex(0);
            }
        }
    }

    /* BROWSER */
    protected void openURL(String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            System.out.println("Navigation failed: " + url);
            throw e;
        }
    }

    protected boolean isButtonEnabled(String locator, String... restParams) {
        if (isElementUndisplayed(locator)) {
            return false;
        }
        return isElementEnable(locator)
                && !"true".equalsIgnoreCase(getAttribute(locator, "aria-disabled"));
    }

    protected boolean isButtonDisabled(String locator, String... restParams) {
        return !isButtonEnabled(locator, restParams);
    }

}