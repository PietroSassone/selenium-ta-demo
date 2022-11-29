package com.example.selenium.ta.demo.drivercreators;

import static com.example.selenium.ta.demo.factory.SeleniumFactory.MOBILE_PLATFORMS;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BrowserStackDriverCreator {

    private static final String BROWSERSTACK_USERNAME = System.getenv("BROWSERSTACK_USERNAME");
    private static final String BROWSERSTACK_ACCESS_KEY = System.getenv("BROWSERSTACK_ACCESS_KEY");
    private static final String BROWSERSTACK_URL = String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub", BROWSERSTACK_USERNAME, BROWSERSTACK_ACCESS_KEY);

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

    public WebDriver createBrowserStackRemoteDriver() {
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
