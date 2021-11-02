# Demo project to demonstrate test automation framework development for UI testing

**Test automation UI framework demo project.**

Simple UI test automation in Java 11, demonstrating how to implement a scalable, flexible framework for running UI tests in parallel.
Supporting different browsers.
Plus saving useful visual test reports.

**1. Technologies used**
- Selenium 4 for UI tests
- Cucumber 6 for Behaviour Specification and Data Driven Testing
- Junit 5 with setting the tests to retry failed ones
- Maven Failsafe plugin to run the test suite
- Awaitility to wait dynamically for elements to reach a desired state
- Logback for logging
- Boni Garcia WebDriver manager
- Spring Core for dependency injection
- Lombok to eliminate a lot of code
- Java Faker for test data generation
- Maven Checkstyle for enforcing coding conventions
- Cucable plugin for cucumber parallel runner generation
- Cluecumber plugin for visualization of test reports

**2. Design patterns used:**
- Behaviour Specification
- Builder
- Page Object Model
- Composition of Page Elements
- Factory:
    * WebDriverFactory
    * Page factory
    * Custom Selenium element locator factory to find web elements even after interactions/changes on a website

**3. Some aspects of UI testing being demonstrated:**
- Using Selenium Data tables
- Checking for the visibility of web elements
- Checking the correctness of links
- Checking the enabled/disabled state of web elements
- Adding data to a web table
- Deleting data from a web table
- Interacting with pagination
- Taking and saving screenshots

**4. Reporting and logging**
- The framework saves reports and logs in the target folder after a test run finishes.
1. Logs are saved in target/logs
1. Cucumber reports are saved in target/cucumber-report
1. More detailed Cluecumber reports are saved in target/test-report

The reports create a visualized overview of the test results. Can be viewed in a browser.
In case of failed scenarios a screenshot of the browser is saved.
The screenshot is added to the test reporting.

**Launching the tests**
Open a terminal and type:
    ```
    mvn clean verify
    ```
Supported arguments:
| argument name     | supported values             | default value | description                                                |
| ----------------- | ---------------------------- | ------------- | ---------------------------------------------------------- |
| browserName       | chrome, firefox, edge, opera | chrome        | tells the tests which browser to use for the tests         |
| headless          | true, false                  | false         | sets whether the tests should run with GUI enabled         |
| rerun.tests.count | any positive integer         | 1             | sets how many times to try rerunning each failed test case |

The framework supports Chrome, Firefox, Edge, Opera browsers for testing.
The headless mode in selenium is not supported in Opera. Only the other 3 browsers.
When trying to start the tests with Opera in headless mode, they'll launch in standard mode.
With an extra log being shown about unsupported headless mode.

*Note:* 
    Some of the tests use selenium implementation interacting with the 'Select' class.
    This is not supported by OperaDriver.
    These tests should not be run with Opera.
    The demo framework has an Opera maven profile that starts all tests that support Opera.
    Skipping the unsupported ones.

   - To run the Opera profile, use the fllowing command:
        ```
        mvn clean verify -POpera
        ```

Setting which tests should be run can be done via the -Dcucumber.filter.tags option.

Example command to run the tests with MS Edge Driver in headless mode:
    ```
    mvn clean verify -DbrowserName=edge -Dheadless=true
    ```

Example command to run the tests with default browser settings (chrome) for only the Web Table page:
    ```
    mvn clean verify -Dcucumber.filter.tags=@WebTablesPage
    ```