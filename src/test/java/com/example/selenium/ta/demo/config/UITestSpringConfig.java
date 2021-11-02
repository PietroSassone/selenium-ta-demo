package com.example.selenium.ta.demo.config;

import com.example.selenium.ta.demo.factory.SeleniumFactory;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.example.selenium.ta.demo")
public class UITestSpringConfig {

    public static final long PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS = 5;

    @Bean(destroyMethod = "shutDownWebDriver")
    public SeleniumFactory seleniumFactory() {
        return new SeleniumFactory();
    }

    @Bean
    public Faker testDataGenerator() {
        return new Faker();
    }
}
