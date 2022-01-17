package com.example.selenium.ta.demo.factory;

import static org.openqa.selenium.remote.CapabilityType.PROXY;

import static com.example.selenium.ta.demo.config.UITestSpringConfig.PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.selenium.ta.demo.util.WebDriverLogger;
import com.example.selenium.ta.demo.util.browserplatform.JsonDeserializerForPlatform;
import com.example.selenium.ta.demo.util.browserplatform.Platform;
import com.example.selenium.ta.demo.util.browserplatform.Screen;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.client.ClientUtil;

/**
 * Factory class to set up and close selenium webdriver.
 */
@Component
public class SeleniumFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumFactory.class);

    private static final String CHROME_BROWSER_NAME = "chrome";
    private static final String FIREFOX_BROWSER_NAME = "firefox";
    private static final String OPERA_BROWSER_NAME = "opera";
    private static final String EDGE_BROWSER_NAME = "edge";
    private static final String BROWSERSTACK = "browserstack";
    private static final String IGNORE_CERTIFICATE_ERRORS_OPTION = "--ignore-certificate-errors";
    private static final String WIDTH_DEVICE_METRIC = "width";
    private static final String HEIGHT_DEVICE_METRIC = "height";
    private static final String PIXEL_RATIO_DEVICE_METRIC = "pixelRatio";
    private static final String DEVICE_METRICS_EMULATION_PARAM = "deviceMetrics";
    private static final String USER_AGENT_EMULATION_PARAM = "userAgent";
    private static final String MOBILE_EMULATION_OPTION_NAME = "mobileEmulation";
    private static final String USER_AGENT_OPTION = "--user-agent=";
    private static final String HEADLESS_OPTION = "headless";
    private static final String DISABLE_GPU_OPTION = "disable-gpu";
    private static final String BROWSERSTACK_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String BROWSERSTACK_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String BROWSERSTACK_URL = String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", BROWSERSTACK_USERNAME, BROWSERSTACK_ACCESS_KEY);
    private static final Set<String> MOBILE_PLATFORMS = Set.of("Android", "IOS");

    @Value("${browserName:chrome}")
    private String browserName;

    @Value("${browserStackBrowserName:Chrome}")
    private String browserStackBrowserName;

    @Value("${browserStackBrowserVersion:latest}")
    private String browserStackBrowserVersion;

    @Value("${browserStackOS:Windows}")
    private String browserStackOS;

    @Value("${browserStackOSVersion:11}")
    private String browserStackOSVersion;

    @Value("${browserStackPlatform}")
    private String browserStackPlatform;

    @Value("${browserStackDevice}")
    private String browserStackDevice;

    @Value("${headless:false}")
    private Boolean headless;

    @Value("${platformToSet:desktop}")
    private String platformToSet;

    @Autowired
    private BrowserMobProxy browserMobProxy;

    @Autowired
    private JsonDeserializerForPlatform deserializerForPlatform;

    private final Function<AbstractDriverOptions<?>, Void> setProxyForWetTrafficRecording = options -> {
        options.setCapability(PROXY, ClientUtil.createSeleniumProxy(browserMobProxy));
        return null;
    };

    private Platform platform;
    private WebDriver webDriver;
    private Map<String, Callable<WebDriver>> webDriverSetupMethodsMap;

    @PostConstruct
    private void setUpWebDriverConfigMethods() {
        webDriverSetupMethodsMap = Map.of(
            CHROME_BROWSER_NAME, this::setUpChromeDriver,
            EDGE_BROWSER_NAME, this::setUpEdgeDriver,
            FIREFOX_BROWSER_NAME, this::setUpFirefoxDriver,
            OPERA_BROWSER_NAME, this::setUpOperaDriver,
            BROWSERSTACK, this::setUpBrowserStackRemoteDriver
        );
    }

    public Platform getPlatform() {
        return platform;
    }

    public WebDriver getExistingWebDriver() {
        return getWebDriver(false);
    }

    public WebDriver createAndGetWebDriver() {
        return getWebDriver(true);
    }

    public WebDriver getNewWebDriverWithPlatform(final String platformNameToSet) {
        shutDownWebDriver();
        platformToSet = platformNameToSet;
        return createAndGetWebDriver();
    }

    public void shutDownWebDriver() {
        if (Objects.nonNull(webDriver)) {
            try {
                webDriver.close();
                webDriver.quit();
                webDriver = null;
            } catch (Exception e) {
                LOGGER.info("Browser already closed, did not need to quit. Exception: {}", e.getMessage());
            }

            webDriver = null;
            LOGGER.info("Selenium WebDriver was shut down.");
        }
    }

    private WebDriver getWebDriver(final boolean shouldCreateNewDriver) {
        if (shouldCreateNewDriver && Objects.isNull(webDriver)) {
            platform = deserializerForPlatform.readJsonFileToPlatform(platformToSet);
            try {
                webDriver = webDriverSetupMethodsMap.get(browserName).call();
            } catch (NullPointerException nullPointerException) {
                throw new RuntimeException("Trying to set up unsupported browser type=" + browserName);
            } catch (Exception e) {
                throw new RuntimeException("Issue when trying to start the driver. Message=" + e.getMessage());
            }

            if (!MOBILE_PLATFORMS.contains(browserStackPlatform)) {
                webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS));
                webDriver.manage().window().setSize(platform.getBrowserWindowSize());

                webDriver = new EventFiringWebDriver(webDriver).register(new WebDriverLogger());
                LOGGER.info("Driver was successfully started for browser {} and platform: {}, with user agent: {}.", browserName, platform.getPlatformName(), platform.getUserAgent());
            }
        }
        return webDriver;
    }

    private WebDriver setUpOperaDriver() {
        WebDriverManager.operadriver().setup();

        if (headless) {
            LOGGER.warn("Opera driver does not support headless mode.");
        }
        OperaOptions operaOptions = new OperaOptions();
        operaOptions.addArguments(IGNORE_CERTIFICATE_ERRORS_OPTION);
        setProxyForWetTrafficRecording.apply(operaOptions);

        if (platform.isDevice()) {
            operaOptions.addArguments(USER_AGENT_OPTION + platform.getUserAgent());
        }

        return new OperaDriver(operaOptions);
    }

    private WebDriver setUpEdgeDriver() {
        WebDriverManager.edgedriver().setup();

        EdgeOptions edgeOptions = new EdgeOptions();
        if (headless) {
            edgeOptions.addArguments(HEADLESS_OPTION);
            edgeOptions.addArguments(DISABLE_GPU_OPTION);
        }
        edgeOptions.addArguments(IGNORE_CERTIFICATE_ERRORS_OPTION);

        setProxyForWetTrafficRecording.apply(edgeOptions);

        if (platform.isDevice()) {
            emulateMobileForChromiumBrowser(edgeOptions);
        }

        return new EdgeDriver(edgeOptions);
    }

    private WebDriver setUpChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(headless);
        chromeOptions.addArguments(IGNORE_CERTIFICATE_ERRORS_OPTION);
        setProxyForWetTrafficRecording.apply(chromeOptions);

        if (platform.isDevice()) {
            emulateMobileForChromiumBrowser(chromeOptions);
        }

        return new ChromeDriver(chromeOptions);
    }

    private WebDriver setUpFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(headless);
        firefoxOptions.addArguments(IGNORE_CERTIFICATE_ERRORS_OPTION);
        setProxyForWetTrafficRecording.apply(firefoxOptions);

        if (platform.isDevice()) {
            firefoxOptions.addArguments(USER_AGENT_OPTION + platform.getUserAgent());
        }

        return new FirefoxDriver(firefoxOptions);
    }

    private ChromiumOptions<?> emulateMobileForChromiumBrowser(final ChromiumOptions<?> chromiumOptions) {
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

    private WebDriver setUpBrowserStackRemoteDriver() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setCapability("os_version", browserStackOSVersion);

        if (MOBILE_PLATFORMS.contains(browserStackPlatform)) {
            setUpCapabilitiesForDeviceBrowserStack(desiredCapabilities);
        } else {
            setUpCapabilitiesForDesktopBrowserStack(desiredCapabilities);
        }

        desiredCapabilities.setCapability("browserstack.debug", "true");
        desiredCapabilities.setCapability("browserstack.console", "info");

        // Needed for automatic HTTP archive capturing
        desiredCapabilities.setCapability("acceptSslCerts", "true");
        desiredCapabilities.setCapability("browserstack.networkLogs", "true");

        // Needed for the test run to appear on the Browserstack dashboard
        desiredCapabilities.setCapability("build", "browserstack-build-1");
        System.out.println(desiredCapabilities);
        WebDriver driver;
        try {
            driver = new RemoteWebDriver(new URL(BROWSERSTACK_URL), desiredCapabilities);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error setting up Browserstack URL.");
        }

        return driver;
    }

    private void setUpCapabilitiesForDesktopBrowserStack(final DesiredCapabilities desiredCapabilities) {
        desiredCapabilities.setCapability("browser", browserStackBrowserName);
        desiredCapabilities.setCapability("browser_version", browserStackBrowserVersion);
        desiredCapabilities.setCapability("os", browserStackOS);
    }

    private void setUpCapabilitiesForDeviceBrowserStack(final DesiredCapabilities desiredCapabilities) {
        desiredCapabilities.setCapability("device", browserStackDevice);
        desiredCapabilities.setCapability("realMobile", "true");
        desiredCapabilities.setCapability("browserName", browserStackPlatform);
    }

}
