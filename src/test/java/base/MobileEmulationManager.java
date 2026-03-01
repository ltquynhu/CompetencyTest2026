package base;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;

public final class MobileEmulationManager {

    private MobileEmulationManager() {
    }

    public static String resolveMobileDevice() {
        String mobileDevice = System.getProperty("MOBILE_DEVICE");
        if (mobileDevice == null || mobileDevice.isBlank()) {
            mobileDevice = System.getenv("MOBILE_DEVICE");
        }
        return mobileDevice;
    }

    public static void apply(ChromeOptions options, String mobileDevice) {
        Map<String, Object> emulation = getMobileEmulationConfig(mobileDevice);
        if (emulation != null) {
            options.setExperimentalOption("mobileEmulation", emulation);
        }
    }

    public static void apply(EdgeOptions options, String mobileDevice) {
        Map<String, Object> emulation = getMobileEmulationConfig(mobileDevice);
        if (emulation != null) {
            options.setExperimentalOption("mobileEmulation", emulation);
        }
    }

    public static boolean isSupported(String mobileDevice) {
        if (mobileDevice == null || mobileDevice.isBlank()) {
            return true;
        }
        String device = mobileDevice.trim().toUpperCase();
        return "IPHONE_13".equals(device) || "GALAXY_S9".equals(device);
    }

    public static String supportedDevicesMessage() {
        return "Supported: IPHONE_13, GALAXY_S9";
    }

    private static Map<String, Object> getMobileEmulationConfig(String mobileDevice) {
        if (mobileDevice == null || mobileDevice.isBlank()) {
            return null;
        }

        String device = mobileDevice.trim().toUpperCase();
        if ("IPHONE_13".equals(device)) {
            return createIphone13Profile();
        }
        if ("GALAXY_S9".equals(device)) {
            return createGalaxyS9Profile();
        }
        return null;
    }

    private static Map<String, Object> createIphone13Profile() {
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 390);
        deviceMetrics.put("height", 844);
        deviceMetrics.put("pixelRatio", 3.0);

        Map<String, Object> emulation = new HashMap<>();
        emulation.put("deviceMetrics", deviceMetrics);
        emulation.put("userAgent",
                "Mozilla/5.0 (iPhone; CPU iPhone OS 15_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.0 Mobile/15E148 Safari/604.1");
        return emulation;
    }

    private static Map<String, Object> createGalaxyS9Profile() {
        Map<String, Object> deviceMetrics = new HashMap<>();
        deviceMetrics.put("width", 360);
        deviceMetrics.put("height", 740);
        deviceMetrics.put("pixelRatio", 4.0);

        Map<String, Object> emulation = new HashMap<>();
        emulation.put("deviceMetrics", deviceMetrics);
        emulation.put("userAgent",
                "Mozilla/5.0 (Linux; Android 10; SM-G960F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36");
        return emulation;
    }
}