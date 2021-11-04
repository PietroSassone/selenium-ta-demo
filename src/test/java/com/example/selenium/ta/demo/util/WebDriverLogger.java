package com.example.selenium.ta.demo.util;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Custom logger for webdriver related events.
 */
public class WebDriverLogger implements WebDriverEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebDriverLogger.class);

    @Override
    public void beforeAlertAccept(WebDriver webDriver) {
    }

    @Override
    public void afterAlertAccept(WebDriver webDriver) {
    }

    @Override
    public void afterAlertDismiss(WebDriver webDriver) {
    }

    @Override
    public void beforeAlertDismiss(WebDriver webDriver) {
    }

    @Override
    public void beforeNavigateTo(String url, WebDriver driver) {
        LOGGER.info("the webdriver will navigate to url={}", url);
    }

    @Override
    public void afterNavigateTo(String url, WebDriver driver) {
        LOGGER.info("the webdriver navigated to url={}", url);
    }

    @Override
    public void beforeNavigateBack(WebDriver driver) {
    }

    @Override
    public void afterNavigateBack(WebDriver driver) {
    }

    @Override
    public void beforeNavigateForward(WebDriver driver) {
    }

    @Override
    public void afterNavigateForward(WebDriver driver) {
    }

    @Override
    public void beforeNavigateRefresh(WebDriver webDriver) {
    }

    @Override
    public void afterNavigateRefresh(WebDriver webDriver) {
    }

    @Override
    public void beforeFindBy(By by, WebElement element, WebDriver driver) {
        if (element == null) {
            LOGGER.info("search for webelement by={}", by.toString());
        } else {
            LOGGER.info("search for webelement={}, by={}", element.toString(), by.toString());
        }
    }

    @Override
    public void afterFindBy(By by, WebElement element, WebDriver driver) {
        if (element == null) {
            LOGGER.info("webelement found by={}", by.toString());
        } else {
            LOGGER.info("webelement={}, found by={}", element.toString(), by.toString());
        }
    }

    @Override
    public void beforeClickOn(WebElement element, WebDriver driver) {
        LOGGER.info("the webdriver will click on webelement={}", element.toString());
    }

    @Override
    public void afterClickOn(WebElement element, WebDriver driver) {
        LOGGER.info("the webdriver clicked on webelement={}", element.toString());
    }

    @Override
    public void beforeChangeValueOf(WebElement element, WebDriver webDriver, CharSequence[] charSequences) {
        LOGGER.info("the webdriver will change value of webelement={}", element.toString());
    }

    @Override
    public void afterChangeValueOf(WebElement element, WebDriver webDriver, CharSequence[] charSequences) {
        LOGGER.info("the webdriver changed value of webelement={}", element.toString());
    }

    @Override
    public void beforeScript(String script, WebDriver driver) {
    }

    @Override
    public void afterScript(String script, WebDriver driver) {
    }

    @Override
    public void beforeSwitchToWindow(String windowName, WebDriver driver) {
    }

    @Override
    public void afterSwitchToWindow(String windowName, WebDriver driver) {
    }

    @Override
    public void onException(Throwable throwable, WebDriver driver) {
        LOGGER.warn("webdriver exception occured={}", throwable.getMessage());
    }

    @Override
    public <X> void beforeGetScreenshotAs(OutputType<X> outputType) {
    }

    @Override
    public <X> void afterGetScreenshotAs(OutputType<X> outputType, X x) {
    }

    @Override
    public void beforeGetText(WebElement webElement, WebDriver webDriver) {
    }

    @Override
    public void afterGetText(WebElement webElement, WebDriver webDriver, String s) {
    }
}
