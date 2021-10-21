package com.example.selenium.ta.demo.factory;

import com.example.selenium.ta.demo.util.WebDriverLogger;
import com.microsoft.edge.seleniumtools.EdgeDriver;
import com.microsoft.edge.seleniumtools.EdgeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * Factory class to set up and close selenium webdriver.
 */
public class SeleniumFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(SeleniumFactory.class);

    private static final String CHROME_BROWSER_NAME = "chrome";
    private static final String FIREFOX_BROWSER_NAME = "firefox";
    private static final String INTERNET_EXPLORER_BROWSER_NAME = "iExplorer";
    private static final String EDGE_BROWSER_NAME = "edge";

    @Value("${browserName:chrome}")
    private String browserName;

    @Value("${headless:false}")
    private Boolean headless;

    private WebDriver webDriver;
    private Map<String, Callable<WebDriver>> webDriverSetupMethodsMap;

    @PostConstruct
    private void setUpWebDriverConfigMethods() {
        webDriverSetupMethodsMap = Map.of(
                CHROME_BROWSER_NAME, this::setUpChromeDriver,
                INTERNET_EXPLORER_BROWSER_NAME, this::setUpIeDriver,
                EDGE_BROWSER_NAME, this::setUpEdgeDriver,
                FIREFOX_BROWSER_NAME, this::setUpFirefoxDriver
        );
    }

    public WebDriver getExistingWebDriver() {
        return getWebDriver(false);
    }

    public WebDriver createAndGetWebDriver() {
        return getWebDriver(true);
    }

    private WebDriver getWebDriver(final boolean shouldCreateNewDriver) {
        if (shouldCreateNewDriver && Objects.isNull(webDriver)) {
            try {
                webDriver = webDriverSetupMethodsMap.get(browserName).call();
            } catch (Exception e) {
                throw new RuntimeException("Trying to set up unsupported browser type=" + browserName);
            }

            webDriver.manage().window().fullscreen();
            webDriver = new EventFiringWebDriver(webDriver).register(new WebDriverLogger());
        }
        return webDriver;
    }

    private WebDriver setUpIeDriver() {
        WebDriverManager.iedriver().setup();
        LOGGER.info("Internet Explorer Driver was successfully started.");

        if (headless) {
            LOGGER.warn("Internet explorer driver does not support headless mode.");
        }

        return new InternetExplorerDriver(new InternetExplorerOptions());
    }

    private WebDriver setUpEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        LOGGER.info("Edge Driver was successfully started.");
        EdgeOptions edgeOptions = new EdgeOptions();
        if (headless) {
            edgeOptions.addArguments("headless");
            edgeOptions.addArguments("disable-gpu");
        }
        return new EdgeDriver(edgeOptions);
    }

    private WebDriver setUpChromeDriver() {
        WebDriverManager.chromedriver().setup();
        LOGGER.info("Chrome Driver was successfully started.");

        return new ChromeDriver(new ChromeOptions().setHeadless(headless));
    }

    private WebDriver setUpFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        LOGGER.info("Firefox Driver was successfully started.");

        return new FirefoxDriver(new FirefoxOptions().setHeadless(headless));
    }

    public void shutDownWebDriver() {
        if (Objects.nonNull(webDriver)) {
            webDriver.close();
            webDriver.quit();
            webDriver = null;
            LOGGER.info("Selenium WebDriver was shut down.");
        }
    }
}
