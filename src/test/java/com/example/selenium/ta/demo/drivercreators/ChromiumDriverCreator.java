package com.example.selenium.ta.demo.drivercreators;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.selenium.ta.demo.util.browserplatform.Platform;
import com.example.selenium.ta.demo.util.browserplatform.Screen;
import io.github.bonigarcia.wdm.WebDriverManager;

@Component
public class ChromiumDriverCreator extends BaseDriverCreator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChromiumDriverCreator.class);

    private static final String WIDTH_DEVICE_METRIC = "width";
    private static final String HEIGHT_DEVICE_METRIC = "height";
    private static final String PIXEL_RATIO_DEVICE_METRIC = "pixelRatio";
    private static final String DEVICE_METRICS_EMULATION_PARAM = "deviceMetrics";
    private static final String USER_AGENT_EMULATION_PARAM = "userAgent";
    private static final String MOBILE_EMULATION_OPTION_NAME = "mobileEmulation";
    private static final String HEADLESS_OPTION = "headless";
    private static final String DISABLE_GPU_OPTION = "disable-gpu";

    public WebDriver createEdgeDriver(final Platform platform) {
        WebDriverManager.edgedriver().setup();

        EdgeOptions edgeOptions = new EdgeOptions();
        if (headless) {
            edgeOptions.addArguments(HEADLESS_OPTION);
            edgeOptions.addArguments(DISABLE_GPU_OPTION);
        }
        edgeOptions.addArguments(IGNORE_CERTIFICATE_ERRORS_OPTION);

        setProxyForWetTrafficRecording.apply(edgeOptions);

        if (platform.isDevice()) {
            emulateMobileForChromiumBrowser(edgeOptions, platform);
        }

        return new EdgeDriver(edgeOptions);
    }

    public WebDriver createChromeDriver(final Platform platform) {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(headless);
        chromeOptions.addArguments(IGNORE_CERTIFICATE_ERRORS_OPTION);
        setProxyForWetTrafficRecording.apply(chromeOptions);

        if (platform.isDevice()) {
            emulateMobileForChromiumBrowser(chromeOptions, platform);
        }

        return new ChromeDriver(chromeOptions);
    }

    private ChromiumOptions<?> emulateMobileForChromiumBrowser(final ChromiumOptions<?> chromiumOptions, final Platform platform) {
        final Screen screen = platform.getScreenSettings();

        final Map<String, Object> deviceMetrics = Map.of(
            WIDTH_DEVICE_METRIC, screen.getScreenWidth(),
            HEIGHT_DEVICE_METRIC, screen.getScreenHeight(),
            PIXEL_RATIO_DEVICE_METRIC, screen.getPixelRatio()
        );

        final Map<String, Object> mobileEmulation = Map.of(
            DEVICE_METRICS_EMULATION_PARAM, deviceMetrics,
            USER_AGENT_EMULATION_PARAM, platform.getUserAgent()
        );

        LOGGER.info("Setting mobile emulation for chromium based driver: {}", mobileEmulation);
        return chromiumOptions.setExperimentalOption(MOBILE_EMULATION_OPTION_NAME, mobileEmulation);
    }
}
