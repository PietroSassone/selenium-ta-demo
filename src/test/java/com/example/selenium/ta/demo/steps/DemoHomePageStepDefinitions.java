package com.example.selenium.ta.demo.steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import java.util.stream.IntStream;

import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.example.selenium.ta.demo.config.UITestSpringConfig;
import com.example.selenium.ta.demo.pageobject.hompage.DemoHomePage;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

@ContextConfiguration(classes = UITestSpringConfig.class)
public class DemoHomePageStepDefinitions {

    private static final String EXPECTED_FOOTER_TEXT = "© 2013-2020 TOOLSQA.COM | ALL RIGHTS RESERVED.";
    private static final String EXPECTED_HEADER_LINK = "https://demoqa.com/";
    private static final String EXPECTED_JOIN_LINK = "https://www.toolsqa.com/selenium-training/";
    private static final String HEADER = "header";

    @Autowired
    private DemoHomePage homePage;

    @Given("^the demo QA homepage is opened$")
    public void theDemoQaHomePageIsOpened() {
        homePage.loadUpHomePage();
    }

    @Then("^the (certification training|header) image should be visible$")
    public void theImageShouldBeVisible(final String imageName) {
        final boolean isDisplayed = imageName.equals(HEADER)
                ? homePage.isHeaderImageVisible()
                : homePage.isCertificationTrainingImageVisible();

        assertThat(String.format("The %s image should be visible on the homepage!", imageName), isDisplayed, is(true));
    }

    @Then("^the (header|join now) link should be correct$")
    public void theLinkShouldBeCorrect(final String linkName) {
        final String actualLink;
        final String expectedLink;

        if (linkName.equals(HEADER)) {
            actualLink = homePage.getHeaderLink();
            expectedLink = EXPECTED_HEADER_LINK;
        } else {
            actualLink = homePage.getJoinLink();
            expectedLink = EXPECTED_JOIN_LINK;
        }
        assertThat(String.format("The %s link should be %s on the homepage", linkName, expectedLink), actualLink, equalTo(expectedLink));
    }

    @And("^there should be (\\d+) category widgets on the page$")
    public void thereShouldBeGivenNumberOfWidgets(final int expectedNumberOfWidgets) {
        assertThat(
                "The number of widgets on the homepage should be: " + expectedNumberOfWidgets,
                homePage.getWidgets().size(),
                is(expectedNumberOfWidgets)
        );
    }

    @And("^the category widgets should be the following in order:$")
    public void theWidgetsShouldBe(final DataTable dataTable) {
        final List<WebElement> widgets = homePage.getWidgets();

        IntStream.range(0, widgets.size()).forEach(
                listIndex -> {
                    final String widgetTitle = widgets.get(listIndex).getText();
                    assertThat("The widget card title should be:" + widgetTitle, widgetTitle, equalTo(dataTable.asList().get(listIndex)));
                }
        );
    }

    @And("^the footer should be visible and correct$")
    public void theFooterShouldBeVisibleAndCorrect() {
        assertThat("The footer should be visible on the homepage!", homePage.isFooterVisible(), is(true));
        assertThat("The footer text should be be correct!", homePage.getFooter().getText(), equalTo(EXPECTED_FOOTER_TEXT));
    }
}
