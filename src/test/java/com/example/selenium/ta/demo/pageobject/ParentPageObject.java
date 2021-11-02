package com.example.selenium.ta.demo.pageobject;

import com.example.selenium.ta.demo.factory.DemoElementLocatorFactory;
import com.example.selenium.ta.demo.factory.SeleniumFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.NoSuchElementException;

import static com.example.selenium.ta.demo.config.UITestSpringConfig.PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS;
import static java.lang.String.valueOf;

public class ParentPageObject {

    private static final String COMPLETE = "complete";
    private static final String RETURN_DOCUMENT_READY_STATE = "return document.readyState";

    private final SeleniumFactory seleniumFactory;
    private final WebDriver driver;

    public ParentPageObject(final SeleniumFactory seleniumFactory) {
        this.seleniumFactory = seleniumFactory;
        this.driver = seleniumFactory.createAndGetWebDriver();
        PageFactory.initElements(new DemoElementLocatorFactory(seleniumFactory), this);
    }

    public ParentPageObject(final SeleniumFactory seleniumFactory, final WebElement parentElement) {
        this.seleniumFactory = seleniumFactory;
        this.driver = seleniumFactory.createAndGetWebDriver();
        PageFactory.initElements(new DefaultElementLocatorFactory(parentElement), this);
    }

    public SeleniumFactory getSeleniumFactory() {
        return seleniumFactory;
    }

    public void moveToElement(final WebElement webElement) {
        new Actions(driver).moveToElement(webElement).build().perform();
    }

    protected void navigateToUrl(final String url) {
        this.driver.get(url);
        waitForPageToLoad();
    }

    protected void click(final WebElement webElementToClick) {
        waitForElementToBeClickable(webElementToClick);
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
