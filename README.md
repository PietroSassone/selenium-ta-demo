# Demo project to demonstrate test automation framework development for UI testing

**Test automation UI framework demo project.**

UI test automation in Java 11, demonstrating how to implement a scalable, flexible framework for running UI tests in parallel.
Supporting different browsers.
Plus saving useful visual test reports and web HTTP traffic records.  

For comparison reasons I have 2 projects containing the same tests, with different implementation:
*1 with Serenity BDD [project](https://github.com/PietroSassone/selenium-serenity-demo) in Java. 
*2 With Selenide Java [project](https://github.com/PietroSassone/java-selenide-demo). 

*Note:* The test cases implemented are not extensive. Just a selection of all possible scenarios.
For a small demo.

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
- BrowserMob for capturing web traffic
- Jackson for deserializing JSON configuration into a Java class

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
- Capturing HTTP web traffic
- Emulating mobile browsers for testing

**4. Reporting and logging**  
The framework saves reports and logs in the target folder after a test run finishes.
1. Logs are saved in target/logs
1. Cucumber reports are saved in target/cucumber-report
1. More detailed Cluecumber reports are saved in target/test-report
1. HTTP Archive is also saved in target/webtraffic for each scenario.
   
The reports create a visualized overview of the test results. Can be viewed in a browser.
In case of failed scenarios a screenshot of the browser is saved.
The screenshot is added to the test reporting.

**5. Pre-requirements for running the tests**
- Have Maven installed.
- Have Java installed, at least version 11.
- Have the latest version of the browser installed that you want to run the tests with.

**6. Launching the tests**  
Open a terminal and type:
    ```
    mvn clean verify
    ```
    
Supported arguments:  
| argument name     | supported values                           | default value | description                                                |
| ----------------- | ------------------------------------------ | ------------- | ---------------------------------------------------------- |
| browserName       | chrome, firefox, edge, opera, browserstack | chrome        | tells the tests which browser to use                       |
| headless          | true, false                                | false         | sets whether the tests should run with GUI enabled         |
| rerun.tests.count | any positive integer                       | 1             | sets how many times to try rerunning each failed test case |
| platformToSet     | desktop, iPhoneX, nexus7                   | desktop       | sets the platform/device to be emulated by the webDriver   |

Plus some arguments only for BrowserStack. See below in point 7.  
The framework supports Chrome, Firefox, Edge, Opera browsers for testing.
The headless mode in selenium is not supported in Opera. Only the other 3 browsers.
When trying to start the tests with Opera in headless mode, they'll launch in standard mode.
With an extra log being shown about unsupported headless mode.

*Notes about OperaDriver:*  
    Some of the tests use selenium implementation interacting with the 'Select' class.
    This is not supported by OperaDriver.
    These tests should not be run with Opera.
    The demo framework has an Opera maven profile that starts all tests that support Opera.
    Skipping the unsupported ones.

   - To run the Opera profile, use the fllowing command:
        ```
        mvn clean verify -POpera
        ```

*Notes about the mobile device emulation:* 
- Tests can be run with the mobile emulation with all supported browsers.
Both in headless and standard mode.
- At the moment, desktop, iPhone X and Nexus 7 tablet views are added to the framework for demo purposes.
- New platforms can be added by creating a JSON config file in the "test/resources/browserplatform" directory.
- The format of a new config added must conform to the same format as the 3 JSON files already present.
- Settings for devices can be copied from: [Chromium devtools devices](https://chromium.googlesource.com/chromium/src/+/167a7f5e03f8b9bd297d2663ec35affa0edd5076/third_party/WebKit/Source/devtools/front_end/emulated_devices/module.json)
- After adding a config for a new device, the tests can be immediately run with this platform.
- When supplying the '-DplatformToSet' param, the value must be the exact same as the JSON file's name.

Setting which tests should be run based on cucumber tags can be done via the ```-Dcucumber.filter.tags option```.

Example command to run the tests with default browser settings (chrome) for only the Web Table page:  
    ```
    mvn clean verify -Dcucumber.filter.tags=@WebTablesPage
    ```

Example command to run the tests with MS Edge Driver in headless mode:  
    ```
    mvn clean verify -DbrowserName=edge -Dheadless=true
    ```

Example command to run the tests with MS Edge Driver while emulating the Nexus 7 tablet browser:  
    ```
    mvn clean verify -DbrowserName=edge -DplatformToSet=nexus7
    ```
    
**7. BrowserStack integration**  

The project also demonstrates browserstack integration.  
To run the test cases on BrowserStack, we need to set our unique BROWSERSTACK_USERNAME and BROWSERSTACK_ACCESS_KEY on our machine as system environment variables.  

To run the tests with BrowserStack desktop or device, add the ```-DbrowserName=browserstack``` param to the mvn verify command.  
To customize the OS, browser and mobile device, use the arguments listed below in the table.  
To get different values for these arguments, check out the [BrowserStack capability generator](https://www.browserstack.com/docs/onboarding/java/getting-started#run-sample-build).

Supported arguments:  
| argument name              | supported values             | default value | description                                                       |
| -------------------------- | ---------------------------- | ------------- | ----------------------------------------------------------------- |
| browserStackBrowserName    | see the BrowserStack website | chrome        | tells BrowserStack which browser to use                           |
| browserStackBrowserVersion | see the BrowserStack website | latest        | sets the desired version of the browser                           |
| browserStackOS             | see the BrowserStack website | Windows       | sets the OS for the tests                                         |
| browserStackOSVersion      | see the BrowserStack website | 11            | sets the OS version for the tests                                 |
| browserStackPlatform       | IOS, Android                 | -             | sets the platform if the tests should run on a device             |
| browserStackDevice         | see the BrowserStack website | -             | sets the device name, should be paired with browserStackPlatform  |

Example command to run the tests with BrowserStack on Edge & OS X:  
    ```
    mvn clean verify -DbrowserName=browserstack -DbrowserStackBrowserName=Edge "-DbrowserStackOS=OS X" "-DbrowserStackOSVersion=Big Sur"
    ```
    
Example command to run the tests with BrowserStack on Google Pixel 6 & Android 12.0:  
    ```
    mvn clean verify -DbrowserName=browserstack -DbrowserStackPlatform=Android -DbrowserStackOSVersion=12.0 "-DbrowserStackDevice=Google Pixel 6"
    ```
 