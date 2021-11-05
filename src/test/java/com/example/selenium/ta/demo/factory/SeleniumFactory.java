package com.example.selenium.ta.demo.factory;

import static org.openqa.selenium.remote.CapabilityType.PROXY;

import static com.example.selenium.ta.demo.config.UITestSpringConfig.PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.example.selenium.ta.demo.util.WebDriverLogger;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.client.ClientUtil;

/**
 * Factory class to set up and close selenium webdriver.
 */
public class SeleniumFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumFactory.class);

    private static final String CHROME_BROWSER_NAME = "chrome";
    private static final String FIREFOX_BROWSER_NAME = "firefox";
    private static final String OPERA_BROWSER_NAME = "opera";
    private static final String EDGE_BROWSER_NAME = "edge";
    private static final String IGNORE_CERTIFICATE_ERRORS = "--ignore-certificate-errors";

    @Value("${browserName:chrome}")
    private String browserName;

    @Value("${headless:false}")
    private Boolean headless;

    @Autowired
    private BrowserMobProxy browserMobProxy;

    private final Function<AbstractDriverOptions<?>, Void> setProxyForWetTrafficRecording = options -> {
        options.setCapability(PROXY, ClientUtil.createSeleniumProxy(browserMobProxy));
        return null;
    };

    private WebDriver webDriver;
    private Map<String, Callable<WebDriver>> webDriverSetupMethodsMap;

    @PostConstruct
    private void setUpWebDriverConfigMethods() {
        webDriverSetupMethodsMap = Map.of(
            CHROME_BROWSER_NAME, this::setUpChromeDriver,
            EDGE_BROWSER_NAME, this::setUpEdgeDriver,
            FIREFOX_BROWSER_NAME, this::setUpFirefoxDriver,
            OPERA_BROWSER_NAME, this::setUpOperaDriver
        );
    }

    public WebDriver getExistingWebDriver() {
        return getWebDriver(false);
    }

    public WebDriver createAndGetWebDriver() {
        return getWebDriver(true);
    }

    public void shutDownWebDriver() {
        if (Objects.nonNull(webDriver)) {
            try {
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
            try {
                webDriver = webDriverSetupMethodsMap.get(browserName).call();
            } catch (NullPointerException nullPointerException) {
                throw new RuntimeException("Trying to set up unsupported browser type=" + browserName);
            } catch (Exception e) {
                throw new RuntimeException("Issue when trying to start the driver. Message=" + e.getMessage());
            }

            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS));
            webDriver.manage().window().setSize(new Dimension(1920, 1080));

            webDriver = new EventFiringWebDriver(webDriver).register(new WebDriverLogger());
            LOGGER.info("Driver was successfully started for browser {}.", browserName);
        }
        return webDriver;
    }

    private WebDriver setUpOperaDriver() {
        WebDriverManager.operadriver().setup();

        if (headless) {
            LOGGER.warn("Opera driver does not support headless mode.");
        }
        OperaOptions operaOptions = new OperaOptions();
        operaOptions.addArguments(IGNORE_CERTIFICATE_ERRORS);
        setProxyForWetTrafficRecording.apply(operaOptions);

        return new OperaDriver(operaOptions);
    }

    private WebDriver setUpEdgeDriver() {
        WebDriverManager.edgedriver().setup();

        EdgeOptions edgeOptions = new EdgeOptions();
        if (headless) {
            edgeOptions.addArguments("headless");
            edgeOptions.addArguments("disable-gpu");
        }
        edgeOptions.addArguments(IGNORE_CERTIFICATE_ERRORS);

        setProxyForWetTrafficRecording.apply(edgeOptions);
        return new EdgeDriver(edgeOptions);
    }

    private WebDriver setUpChromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(headless);
        chromeOptions.addArguments(IGNORE_CERTIFICATE_ERRORS);
        setProxyForWetTrafficRecording.apply(chromeOptions);

        return new ChromeDriver(chromeOptions);
    }

    private WebDriver setUpFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();

        FirefoxOptions firefoxOptions = new FirefoxOptions();
        firefoxOptions.setHeadless(headless);
        firefoxOptions.addArguments(IGNORE_CERTIFICATE_ERRORS);
        setProxyForWetTrafficRecording.apply(firefoxOptions);

        return new FirefoxDriver(firefoxOptions);
    }
}
