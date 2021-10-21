package com.example.selenium.ta.demo.pageobject;

import com.example.selenium.ta.demo.factory.DemoElementLocatorFactory;
import com.example.selenium.ta.demo.factory.SeleniumFactory;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.NoSuchElementException;

import static com.example.selenium.ta.demo.config.UITestSpringConfig.PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS;
import static java.lang.String.valueOf;

public class ParentPageObject {

    private static final String COMPLETE = "complete";
    private static final String RETURN_DOCUMENT_READY_STATE = "return document.readyState";

    protected WebDriver driver;

    public ParentPageObject(final SeleniumFactory seleniumFactory) {
        driver = seleniumFactory.createAndGetWebDriver();
        PageFactory.initElements(new DemoElementLocatorFactory(seleniumFactory), this);
    }

    public void waitForPageToLoad() {
        new WebDriverWait(this.driver, PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS).until(
                driver -> valueOf(((JavascriptExecutor) driver).executeScript(RETURN_DOCUMENT_READY_STATE)).equals(COMPLETE)
        );
    }

    public void waitForElementToBeClickable(final WebElement webElement) {
        try {
            getWebDriverWait().until(ExpectedConditions.elementToBeClickable(webElement));
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Element is not clickable!");
        }
    }

    private WebDriverWait getWebDriverWait() {
        return new WebDriverWait(this.driver, PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS);
    }

    protected void navigateToUrl(final String url) {
        this.driver.get(url);
        waitForPageToLoad();
    }
}
