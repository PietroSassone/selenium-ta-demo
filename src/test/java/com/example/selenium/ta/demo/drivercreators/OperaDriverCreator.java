package com.example.selenium.ta.demo.drivercreators;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.selenium.ta.demo.util.browserplatform.Platform;
import io.github.bonigarcia.wdm.WebDriverManager;

@Component
public class OperaDriverCreator extends BaseDriverCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(OperaDriverCreator.class);

    public WebDriver createOperaDriver(final Platform platform) {
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
}
