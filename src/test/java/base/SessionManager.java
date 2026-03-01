package base;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public final class SessionManager {

    private static final Map<String, Set<Cookie>> COOKIE_STORE = new ConcurrentHashMap<>();

    private SessionManager() {
    }

    public static void saveSession(WebDriver driver, String userKey) {
        if (driver == null || userKey == null || userKey.isBlank()) {
            return;
        }
        Set<Cookie> cookies = driver.manage().getCookies();
        if (cookies != null && !cookies.isEmpty()) {
            COOKIE_STORE.put(userKey, cookies);
        }
    }

    public static boolean restoreSession(WebDriver driver, String appUrl, String userKey) {
        if (driver == null || appUrl == null || appUrl.isBlank() || userKey == null || userKey.isBlank()) {
            return false;
        }

        Set<Cookie> cookies = COOKIE_STORE.get(userKey);
        if (cookies == null || cookies.isEmpty()) {
            return false;
        }

        driver.get(appUrl);
        driver.manage().deleteAllCookies();
        for (Cookie cookie : cookies) {
            try {
                driver.manage().addCookie(cookie);
            } catch (Exception ignore) {
            }
        }
        driver.navigate().refresh();
        return true;
    }

    public static void clearAll() {
        COOKIE_STORE.clear();
    }
}