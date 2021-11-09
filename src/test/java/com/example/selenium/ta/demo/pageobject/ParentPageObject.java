package com.example.selenium.ta.demo.pageobject;

import static java.lang.String.valueOf;

import static com.example.selenium.ta.demo.config.UITestSpringConfig.PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS;

import java.time.Duration;
import java.util.NoSuchElementException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.selenium.ta.demo.factory.DemoElementLocatorFactory;
import com.example.selenium.ta.demo.factory.SeleniumFactory;
import com.example.selenium.ta.demo.util.WebTrafficRecorder;

/**
 * Class to store common reusable methods and the Page Factory for all page objects.
 */
public class ParentPageObject {

    private static final String COMPLETE = "complete";
    private static final String RETURN_DOCUMENT_READY_STATE = "return document.readyState";
    private static final String SCROLL_INTO_VIEW_SCRIPT = "arguments[0].scrollIntoView();";

    private final SeleniumFactory seleniumFactory;
    private final WebDriver driver;

    @Autowired
    private WebTrafficRecorder webTrafficRecorder;

    /**
     * Base constructor to initialize all page objects extending this class.
     */
    public ParentPageObject(final SeleniumFactory seleniumFactory) {
        this.seleniumFactory = seleniumFactory;
        this.driver = seleniumFactory.createAndGetWebDriver();
        PageFactory.initElements(new DemoElementLocatorFactory(seleniumFactory), this);
    }

    /**
     * Additional constructor to initialize child elements of page objects.
     * Stored in separate classes.
     * For implementing the Composition of Page Objects design pattern.
     */
    public ParentPageObject(final SeleniumFactory seleniumFactory, final WebElement parentElement) {
        this.seleniumFactory = seleniumFactory;
        this.driver = seleniumFactory.createAndGetWebDriver();
        PageFactory.initElements(new DefaultElementLocatorFactory(parentElement), this);
    }

    public SeleniumFactory getSeleniumFactory() {
        return seleniumFactory;
    }

    /**
     * Scrolling to a given web element.
     */
    public void moveToElement(final WebElement webElement) {
        waitForElementToBeClickable(webElement);
        new Actions(driver).moveToElement(webElement).build().perform();
    }

    /**
     * Scrolling to a given web element. In case of some web elements, the regular scrolling doesn't work.
     * Especially useful for firefox.
     */
    public void moveToElementWithJs(final WebElement webElement) {
        ((JavascriptExecutor) driver).executeScript(SCROLL_INTO_VIEW_SCRIPT, webElement);
    }

    protected void navigateToUrl(final String url) {
        webTrafficRecorder.startRecordingTraffic();
        this.driver.get(url);
        waitForPageToLoad();
    }

    protected void click(final WebElement webElementToClick) {
        waitForElementToBeClickable(webElementToClick);
        moveToElementWithJs(webElementToClick);
        webElementToClick.click();
        waitForPageToLoad();
    }

    protected String getReferenceOfElement(final WebElement element) {
        return element.getAttribute("href");
    }

    private WebDriverWait getWebDriverWait() {
        return new WebDriverWait(this.driver, Duration.ofSeconds(PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS));
    }

    private void waitForElementToBeClickable(final WebElement webElement) {
        try {
            getWebDriverWait().until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Element is not clickable!");
        }
    }

    private void waitForPageToLoad() {
        new WebDriverWait(this.driver, Duration.ofSeconds(PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS)).until(
            driver -> valueOf(((JavascriptExecutor) driver).executeScript(RETURN_DOCUMENT_READY_STATE)).equals(COMPLETE)
        );
    }
}
