package com.example.selenium.ta.demo.factory;

import static com.example.selenium.ta.demo.config.UITestSpringConfig.PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.annotation.PostConstruct;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.selenium.ta.demo.drivercreators.BrowserStackDriverCreator;
import com.example.selenium.ta.demo.drivercreators.ChromiumDriverCreator;
import com.example.selenium.ta.demo.drivercreators.FirefoxDriverCreator;
import com.example.selenium.ta.demo.drivercreators.OperaDriverCreator;
import com.example.selenium.ta.demo.util.WebDriverLogger;
import com.example.selenium.ta.demo.util.browserplatform.JsonDeserializerForPlatform;
import com.example.selenium.ta.demo.util.browserplatform.Platform;

/**
 * Factory class to set up and close selenium webdriver.
 */
@Component
public class SeleniumFactory {
    public static final Set<String> MOBILE_PLATFORMS = Set.of("Android", "IOS");

    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumFactory.class);

    private static final String CHROME_BROWSER_NAME = "chrome";
    private static final String FIREFOX_BROWSER_NAME = "firefox";
    private static final String OPERA_BROWSER_NAME = "opera";
    private static final String EDGE_BROWSER_NAME = "edge";
    private static final String BROWSERSTACK = "browserstack";

    @Value("${browserName:chrome}")
    private String browserName;

    @Value("${platformToSet:desktop}")
    private String platformToSet;

    @Value("${browserStackPlatform}")
    private String browserStackPlatform;

    @Autowired
    private JsonDeserializerForPlatform deserializerForPlatform;

    @Autowired
    private ChromiumDriverCreator chromiumDriverCreator;

    @Autowired
    private FirefoxDriverCreator firefoxDriverCreator;

    @Autowired
    private OperaDriverCreator operaDriverCreator;

    @Autowired
    private BrowserStackDriverCreator browserStackDriverCreator;

    private Platform platform;
    private WebDriver webDriver;
    private Map<String, Callable<WebDriver>> webDriverSetupMethodsMap;

    @PostConstruct
    private void setUpWebDriverConfigMethods() {
        webDriverSetupMethodsMap = Map.of(
            CHROME_BROWSER_NAME, this::createChromeDriverWithPlatform,
            EDGE_BROWSER_NAME, this::createEdgeDriverWithPlatform,
            FIREFOX_BROWSER_NAME, this::createFirefoxDriverWithPlatform,
            OPERA_BROWSER_NAME, this::createOperaDriverWithPlatform,
            BROWSERSTACK, browserStackDriverCreator::createBrowserStackRemoteDriver
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

    private WebDriver createChromeDriverWithPlatform() {
        return chromiumDriverCreator.createChromeDriver(platform);
    }

    private WebDriver createEdgeDriverWithPlatform() {
        return chromiumDriverCreator.createEdgeDriver(platform);
    }

    private WebDriver createFirefoxDriverWithPlatform() {
        return firefoxDriverCreator.createFirefoxDriver(platform);
    }

    private WebDriver createOperaDriverWithPlatform() {
        return operaDriverCreator.createOperaDriver(platform);
    }
}
