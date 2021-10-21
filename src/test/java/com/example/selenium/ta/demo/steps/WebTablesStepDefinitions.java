package com.example.selenium.ta.demo.steps;

import com.example.selenium.ta.demo.config.UITestSpringConfig;
import com.example.selenium.ta.demo.pageobject.WebTableRow;
import com.example.selenium.ta.demo.pageobject.WebTablesPage;
import com.github.javafaker.Faker;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@ContextConfiguration(classes = UITestSpringConfig.class)
public class WebTablesStepDefinitions {

    private static final String ADD_NEW_RECORD = "add new record";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String AGE = "age";
    private static final String SALARY = "salary";
    private static final String DEPARTMENT = "department";

    @Autowired
    private WebTablesPage webTablesPage;

    @Autowired
    private Faker testDataGenerator;

    private final Map<String, String> inputFieldsAndValuesMap = new HashMap<>();

    @After
    public void resetWebTablesTestData() {
        inputFieldsAndValuesMap.clear();
    }

    @Given("^the web tables page is opened$")
    public void theWebTablesPageIsOpened() {
        webTablesPage.loadUpWebTablesPage();
    }

    @When("^the (add new record|submit) button is clicked$")
    public void theAddNewRecordButtonIsCLicked(final String button) {
        if (ADD_NEW_RECORD.equals(button)) {
            webTablesPage.clickOnAddNewRecordButton();
        } else {
            webTablesPage.clickOnSubmitButton();
        }
    }

    @When("valid input is prepared for the add table row form")
    public void validInputIsPreparedForTheAddTableRowForm() {
        inputFieldsAndValuesMap.put(FIRST_NAME, testDataGenerator.name().firstName());
        inputFieldsAndValuesMap.put(LAST_NAME, testDataGenerator.name().lastName());
        inputFieldsAndValuesMap.put(AGE, String.valueOf(testDataGenerator.number().numberBetween(1, 150)));
        inputFieldsAndValuesMap.put(EMAIL, testDataGenerator.internet().emailAddress());
        inputFieldsAndValuesMap.put(SALARY, String.valueOf(testDataGenerator.number().numberBetween(0, Integer.MAX_VALUE)));
        inputFieldsAndValuesMap.put(DEPARTMENT, testDataGenerator.company().industry());
    }

    @When("^the (firstName|lastName|email|age|salary|department) input field is filled with (.*)$")
    public void theInputFieldIsFilled(final String fieldName, final String valueToSet) {
        webTablesPage.getInputFieldByName(fieldName).sendKeys(valueToSet);
        inputFieldsAndValuesMap.remove(fieldName);
    }

    @When("^all(?: other)? input fields are filled with the prepared valid input$")
    public void allInputFieldsAreFilledWithValidInput() {
        inputFieldsAndValuesMap.forEach((key, value) -> webTablesPage.getInputFieldByName(key).sendKeys(value));
    }

    @Then("^(\\d) new row(?:s)? should be present with the prepared values$")
    public void aNewRowShouldBePresentWithValues(final int expectedNumberOfNewRows) {
        final WebTableRow expectedRow = WebTableRow.builder()
                .firstName(inputFieldsAndValuesMap.get(FIRST_NAME))
                .lastName(inputFieldsAndValuesMap.get(LAST_NAME))
                .age(Integer.parseInt(inputFieldsAndValuesMap.get(AGE)))
                .email(inputFieldsAndValuesMap.get(EMAIL))
                .salary(Integer.parseInt(inputFieldsAndValuesMap.get(SALARY)))
                .department(inputFieldsAndValuesMap.get(DEPARTMENT))
                .build();

        final List<WebTableRow> matchingWebTableRows = webTablesPage.getWebTableRows().stream()
                .map(
                        row -> WebTableRow.builder()
                        .firstName(getNthChildDivText(1).apply(row))
                        .lastName(getNthChildDivText(2).apply(row))
                        .age(getNthChildDivNumber(3).apply(row))
                        .email(getNthChildDivText(4).apply(row))
                        .salary(getNthChildDivNumber(5).apply(row))
                        .department(getNthChildDivText(6).apply(row))
                        .build()
                )
                .filter(expectedRow::equals)
                .collect(Collectors.toList());

        assertThat(
                String.format(
                        "There should be %s rows matching the submitted input: %s, but the rows were: %s.",
                        expectedNumberOfNewRows,
                        expectedRow,
                        matchingWebTableRows
                ),
                matchingWebTableRows,
                hasSize(expectedNumberOfNewRows)
        );
    }

    @Then("^there should be (\\d+) rows in the table$")
    public void thereShouldBeGivenNumberOfRowsInTheTable(final int expectedNumberOfRows) {
        assertThat(
                String.format("There should be exactly %s rows in the table.", expectedNumberOfRows),
                webTablesPage.getWebTableRows(),
                hasSize(expectedNumberOfRows)
        );
    }

    private Function<WebElement, String> getNthChildDivText(final int indexOfDivChildElement) {
        return webElement -> webElement.findElement(By.cssSelector(String.format("div:nth-child(%s)", indexOfDivChildElement))).getText();
    }

    private Function<WebElement, Integer> getNthChildDivNumber(final int indexOfDivChildElement) {
        return webElement -> Integer.valueOf(getNthChildDivText(indexOfDivChildElement).apply(webElement));
    }
}
