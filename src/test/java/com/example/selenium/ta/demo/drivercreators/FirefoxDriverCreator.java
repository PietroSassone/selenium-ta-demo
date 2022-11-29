package com.example.selenium.ta.demo.drivercreators;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.stereotype.Component;

import com.example.selenium.ta.demo.util.browserplatform.Platform;
import io.github.bonigarcia.wdm.WebDriverManager;

@Component
public class FirefoxDriverCreator extends BaseDriverCreator {

    public WebDriver createFirefoxDriver(final Platform platform) {
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

}
