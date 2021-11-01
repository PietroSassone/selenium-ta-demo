package com.example.selenium.ta.demo.hooks;

import com.example.selenium.ta.demo.factory.SeleniumFactory;
import com.example.selenium.ta.demo.util.ScreenshotSaver;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private SeleniumFactory seleniumFactory;

    @Autowired
    private ScreenshotSaver screenshotSaver;

    @After
    public void cleanUp(final Scenario scenario) {
        if (scenario.isFailed()){
            screenshotSaver.addScreenshotToCucumberScenario(scenario, seleniumFactory.getExistingWebDriver());
        }
    }
}
