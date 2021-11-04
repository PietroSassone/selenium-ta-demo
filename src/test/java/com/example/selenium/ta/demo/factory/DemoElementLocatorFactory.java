package com.example.selenium.ta.demo.factory;

import java.lang.reflect.Field;

import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.selenium.ta.demo.util.DemoElementLocator;

/**
 * Factory class to set up the custom element locator class.
 */
public class DemoElementLocatorFactory implements ElementLocatorFactory {

    @Autowired
    private final SeleniumFactory seleniumFactory;


    public DemoElementLocatorFactory(final SeleniumFactory seleniumFactory) {
        this.seleniumFactory = seleniumFactory;
    }

    @Override
    public ElementLocator createLocator(final Field field) {
        return new DemoElementLocator(seleniumFactory, field);
    }
}
