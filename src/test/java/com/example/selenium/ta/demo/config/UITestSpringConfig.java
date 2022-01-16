package com.example.selenium.ta.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.example.selenium.ta.demo.factory.BrowserMobProxyFactory;
import com.example.selenium.ta.demo.util.browserplatform.JsonDeserializerForPlatform;
import com.example.selenium.ta.demo.util.browserplatform.Platform;
import com.github.javafaker.Faker;
import net.lightbody.bmp.BrowserMobProxy;

@Configuration
@ComponentScan("com.example.selenium.ta.demo")
public class UITestSpringConfig {

    public static final long PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS = 5;

    @Bean
    public Faker testDataGenerator() {
        return new Faker();
    }

    @Bean(destroyMethod = "stopBrowserMobProxy")
    public BrowserMobProxyFactory browserMobProxyFactory() {
        return new BrowserMobProxyFactory();
    }

    @Bean
    public BrowserMobProxy browserMobProxy() {
        return browserMobProxyFactory().getBrowserMobProxy();
    }

    @Bean
    public JsonDeserializerForPlatform deserializerForPlatform() {
        return new JsonDeserializerForPlatform(Platform.class);
    }
}
