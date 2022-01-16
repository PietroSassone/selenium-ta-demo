package com.example.selenium.ta.demo.hooks;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.selenium.ta.demo.factory.SeleniumFactory;
import com.example.selenium.ta.demo.util.ScreenshotSaver;
import com.example.selenium.ta.demo.util.WebTrafficRecorder;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;

public class Hooks {

    @Autowired
    private SeleniumFactory seleniumFactory;

    @Autowired
    private ScreenshotSaver screenshotSaver;

    @Autowired
    private WebTrafficRecorder webTrafficRecorder;

    @After
    public void cleanUp(final Scenario scenario) {
        webTrafficRecorder.saveHttpArchiveToFile(scenario);

        if (scenario.isFailed()){
            screenshotSaver.addScreenshotToCucumberScenario(scenario, seleniumFactory.getExistingWebDriver());
        }

        seleniumFactory.shutDownWebDriver();
    }
}
