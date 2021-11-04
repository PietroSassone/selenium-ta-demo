package com.example.selenium.ta.demo.util;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import com.example.selenium.ta.demo.factory.SeleniumFactory;

/**
 * Utility class to create a custom implementation of the Selenium ElementLocator.
 * Used to find and cache webelements even after changes on the page.
 */
public class DemoElementLocator implements ElementLocator {

    private final SeleniumFactory seleniumFactory;
    private final boolean shouldCacheElement;
    private final By selectorMethod;
    private WebElement cachedElement;
    private List<WebElement> cachedElements;

    public DemoElementLocator(final SeleniumFactory seleniumFactory, final Field field) {
        this.seleniumFactory = seleniumFactory;
        final Annotations annotations = new Annotations(field);

        shouldCacheElement = annotations.isLookupCached();
        selectorMethod = annotations.buildBy();
    }

    @Override
    public WebElement findElement() {
        WebElement element;
        if (Objects.nonNull(cachedElement) && shouldCacheElement) {
            element = cachedElement;
        } else {
            element = seleniumFactory.getExistingWebDriver().findElement(selectorMethod);
            if (shouldCacheElement) {
                cachedElement = element;
            }
        }
        return element;
    }

    @Override
    public List<WebElement> findElements() {
        List<WebElement> elements;
        if (Objects.nonNull(cachedElements) && shouldCacheElement) {
            elements = cachedElements;
        } else {
            elements = seleniumFactory.getExistingWebDriver().findElements(selectorMethod);
            if (shouldCacheElement) {
                cachedElements = elements;
            }
        }
        return elements;
    }
}
