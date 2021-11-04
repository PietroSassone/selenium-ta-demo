package com.example.selenium.ta.demo;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;

import com.example.selenium.ta.demo.config.UITestSpringConfig;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "target/parallel/features/[CUCABLE:FEATURE].feature",
        glue = "com.example.selenium.ta.demo",
        plugin = {"pretty", "json:target/cucumber-report/[CUCABLE:RUNNER].json"}
)
@CucumberContextConfiguration
@ContextConfiguration(classes = UITestSpringConfig.class)
public class RunCucumberTestsIT {

}
