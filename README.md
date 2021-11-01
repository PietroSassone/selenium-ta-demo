# Demo project to demonstrate test automation framework development for UI testing

**Test automation UI framework demo project.**

Simple UI test automation in Java 11, demonstrating how to implement a scalable, flexible framework for running UI tests in parallel.
Supporting different browsers.
Plus saving useful visual test reports.

**Technologies used**
- Selenium for UI tests
- Cucumber 6
- Junit 5 with setting the tests to retry failed ones
- Maven Failsafe plugin to run the test suite
- Logback for logging
- Boni Garcia WebDriver manager
- Spring Core for dependency injection
- Lombok to eliminate a lot of code
- Java Faker for test data generation
- Maven Checkstyle for enforcing coding conventions
- Cucable plugin for cucumber parallel runner generation
- Cluecumber plugin for visualization of test reports

**Design patterns used:**
- Builder
- Page Object Pattern with page factory
- Factory:
    
    1.WebDriverFactory pattern
    1.Custom Selenium element locator factory to find web elements even after interactions/changes on a website

**Some aspects of UI testing being demonstrated:**
- Using Selenium Data tables
- Checking for the visibility of web elements
- Checking the correctness of links
- Checking the enabled/disabled state of web elements
- Adding data to a web table
- Deleting data from a web table
- Interacting with pagination
- Taking and saving screenshots

**Reporting and logging**
- The framework saves reports and logs in the target folder after a test run finishes.
1. Logs are saved in target/logs
1. Cucumber reports are saved in target/cucumber-report
1. More detailed Cluecumber reports are saved in target/test-report

The reports create a visualized overview of the test results. Can be viewed in a browser.
In case of failed scenarios a screenshot of the browser is saved.
The screenshot is added to the test reporting.

**Launching the tests**
Open a terminal and type:
mvn clean verify