package com.example.selenium.ta.demo.steps;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.selenium.ta.demo.pageobject.webtablespage.WebTablesPage;
import io.cucumber.java.After;
import io.cucumber.java.en.When;

public class CommonStepDefinitions {

    private static final String DESKTOP = "desktop";

    @Autowired
    private WebTablesPage webTablesPage;

    @After("@platformChangeInScenario")
    public void resetDriverPlatform() {
        changeDriverPlatform(DESKTOP);
    }

    @When("^the platform is switched to (iPhoneX|nexus7)$")
    public void thePlatformIsSwitched(final String platformToSet) {
        if (!webTablesPage.getSeleniumFactory().getPlatform().getPlatformName().equals(platformToSet)) {
            changeDriverPlatform(platformToSet);
        }
    }

    private void changeDriverPlatform(final String newPlatformName) {
        webTablesPage.getSeleniumFactory().getNewWebDriverWithPlatform(newPlatformName);
    }
}
