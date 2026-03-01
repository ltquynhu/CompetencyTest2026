package base;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public final class DriverFactory {

    private DriverFactory() {
    }

    public static String resolveBrowser() {
        String browser = System.getProperty("BROWSER");
        if (browser != null && !browser.isBlank()) {
            return browser;
        }

        browser = System.getenv("BROWSER");
        if (browser != null && !browser.isBlank()) {
            return browser;
        }

        return "CHROME";
    }

    public static WebDriver createDriver(String browser, String mobileDevice) {
        switch (browser.toUpperCase()) {
            case "H_CHROME":
                ChromeOptions headlessChrome = new ChromeOptions();
                headlessChrome.addArguments("headless");
                headlessChrome.addArguments("window-size=1920x1080");
                MobileEmulationManager.apply(headlessChrome, mobileDevice);
                return new ChromeDriver(headlessChrome);

            case "EDGE":
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("window-size=1920x1080");
                edgeOptions.addArguments("--start-maximized");
                MobileEmulationManager.apply(edgeOptions, mobileDevice);
                return new EdgeDriver(edgeOptions);

            case "H_EDGE":
                EdgeOptions headlessEdge = new EdgeOptions();
                headlessEdge.addArguments("--headless=new");
                headlessEdge.addArguments("window-size=1920x1080");
                MobileEmulationManager.apply(headlessEdge, mobileDevice);
                return new EdgeDriver(headlessEdge);

            case "CHROME":
            default:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-orgins=*");
                chromeOptions.addArguments("window-size=1920x1080");
                chromeOptions.addArguments("--start-maximized");
                MobileEmulationManager.apply(chromeOptions, mobileDevice);
                return new ChromeDriver(chromeOptions);
        }
    }
}