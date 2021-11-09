package com.example.selenium.ta.demo.steps;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.openqa.selenium.Keys.ENTER;

import static com.example.selenium.ta.demo.config.UITestSpringConfig.PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import com.example.selenium.ta.demo.config.UITestSpringConfig;
import com.example.selenium.ta.demo.pageobject.webtablespage.WebTablePagination;
import com.example.selenium.ta.demo.pageobject.webtablespage.WebTableRow;
import com.example.selenium.ta.demo.pageobject.webtablespage.WebTablesPage;
import com.github.javafaker.Faker;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ContextConfiguration(classes = UITestSpringConfig.class)
public class WebTablesStepDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebTablesStepDefinitions.class);

    private static final String ADD_NEW_RECORD = "add new record";
    private static final String SUBMIT = "submit";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String AGE = "age";
    private static final String SALARY = "salary";
    private static final String DEPARTMENT = "department";
    private static final String PREVIOUS = "previous";
    private static final String ENABLED = "enabled";
    private static final String NO_ROWS_FOUND_MESSAGE = "No rows found";

    private final Map<String, String> inputFieldsAndValuesMap = new HashMap<>();

    @Autowired
    private WebTablesPage webTablesPage;

    @Autowired
    private Faker testDataGenerator;

    @After("@WebTablesPage")
    public void resetWebTablesTestData() {
        inputFieldsAndValuesMap.clear();
    }

    @Given("^the web tables page is opened$")
    public void theWebTablesPageIsOpened() {
        webTablesPage.loadUpWebTablesPage();
    }

    @When("^the (add new record|submit) button is clicked$")
    public void theAddNewRecordOrSubmitButtonIsClicked(final String button) {
        if (ADD_NEW_RECORD.equals(button)) {
            webTablesPage.clickOnAddNewRecordButton();
        } else {
            webTablesPage.clickOnSubmitButton();
        }
    }

    @When("^the prepared test data is added to the table (\\d+) times$")
    public void theAddNewRecordOrSubmitButtonIsClicked(final int numberOfTimesToAddRow) {
        IntStream.range(0, numberOfTimesToAddRow).forEach(
            occurrenceIndex -> {
                theAddNewRecordOrSubmitButtonIsClicked(ADD_NEW_RECORD);
                allInputFieldsAreFilledWithValidInput();
                theAddNewRecordOrSubmitButtonIsClicked(SUBMIT);
            }
        );
    }

    @When("^the (previous|next) page pagination button is clicked$")
    public void thePaginationButtonIsClicked(final String button) {
        if (PREVIOUS.equals(button)) {
            webTablesPage.getWebTablePagination().clickOnPreviousPaginationButton();
        } else {
            webTablesPage.getWebTablePagination().clickOnNextPaginationButton();
        }
    }

    @When("valid input is prepared for the add table row form")
    public void validInputIsPreparedForTheAddTableRowForm() {
        inputFieldsAndValuesMap.put(FIRST_NAME, testDataGenerator.name().firstName());
        inputFieldsAndValuesMap.put(LAST_NAME, testDataGenerator.name().lastName());
        inputFieldsAndValuesMap.put(AGE, String.valueOf(testDataGenerator.number().numberBetween(1, 99)));
        inputFieldsAndValuesMap.put(EMAIL, testDataGenerator.internet().emailAddress());
        inputFieldsAndValuesMap.put(SALARY, String.valueOf(testDataGenerator.number().numberBetween(0, Integer.MAX_VALUE)));
        inputFieldsAndValuesMap.put(DEPARTMENT, testDataGenerator.lorem().characters(1, 20));
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

    @When("^the delete button for item number (\\d+) is clicked$")
    public void theDeleteButtonForTheGivenItemIsClicked(final int numberOfRowToDelete) {
        final WebElement deleteButtonToClick = webTablesPage.getDeleteButtons().get(numberOfRowToDelete - 1);
        webTablesPage.moveToElementWithJs(deleteButtonToClick);
        deleteButtonToClick.click();
    }

    @When("^the display results per page dropdown is set to (5|10|20|25|50|100) rows$")
    public void theDisplayResultsPerPageDropdownIsSetTo(final String numberOfRowsToDisplay) {
        webTablesPage.getWebTablePagination().getResultsPerPageDropDownSelect().selectByValue(numberOfRowsToDisplay);
    }

    @When("^the current page index is set to (.*)$")
    public void theCurrentPageIndexIsSetTo(final String pageIndexToSet) {
        final WebElement pageIndexInput = webTablesPage.getWebTablePagination().getPageJumpField();
        pageIndexInput.sendKeys(pageIndexToSet);
        pageIndexInput.sendKeys(ENTER);
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
            .peek(row -> LOGGER.info("Row found on the page: {}", row))
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

    @Then("^there should be (\\d+) row(?:s)? in the table on the current page$")
    public void thereShouldBeGivenNumberOfRowsInTheTable(final int expectedNumberOfRows) {
        assertThat(
            String.format("There should be exactly %s rows in the table.", expectedNumberOfRows),
            webTablesPage.getWebTableRows(),
            hasSize(expectedNumberOfRows)
        );
    }

    @Then("^the 'No rows found' text should be visible in the table$")
    public void thereShouldBeGivenNumberOfRowsInTheTable() {
        assertThat("The 'No rows found' text should be visible in the table.", webTablesPage.isNoDataTextElementVisible(), is(true));
        assertThat("The field's text should be 'No rows found'.", webTablesPage.getNoDataTextElement().getText(), equalTo(NO_ROWS_FOUND_MESSAGE));
    }

    @Then("^the total number of pages in the table should be (\\d)$")
    public void theTotalNumberOfPagesInTheTableShouldBe(final int expectedNumberOfPages) {
        await().atMost(Duration.ofSeconds(PAGE_OR_ELEMENT_LOAD_WAIT_SECONDS))
            .until(() -> webTablesPage.getWebTablePagination().getTotalNumberOfTablePages().getText(), not(is(emptyOrNullString())));

        final WebElement totalNumberOfPages = webTablesPage.getWebTablePagination().getTotalNumberOfTablePages();
        webTablesPage.moveToElement(totalNumberOfPages);
        assertThat(
            String.format("There should be exactly %s pages in the table.", expectedNumberOfPages),
            totalNumberOfPages.getText(),
            equalTo(String.valueOf(expectedNumberOfPages))
        );
    }

    @Then("^the page index input field of the pagination should contain (\\d)$")
    public void thePageIndexPaginationFieldShouldContain(final int expectedCurrentPageIndex) {
        assertThat(
            String.format("The current page index should be %s.", expectedCurrentPageIndex),
            webTablesPage.getWebTablePagination().getPageJumpField().getAttribute("value"),
            equalTo(String.valueOf(expectedCurrentPageIndex))
        );
    }

    @Then("^the (previous|next) page pagination button should be (enabled|disabled)$")
    public void thePaginationButtonShouldBeEnabledOrDisabled(final String buttonName, final String expectedState) {
        final WebTablePagination paginationBar = webTablesPage.getWebTablePagination();

        final WebElement paginationButton = PREVIOUS.equals(buttonName)
            ? paginationBar.getPreviousPageButton()
            : paginationBar.getNextPageButton();

        assertThat(
            String.format("The %s pagination button should be in %s state.", buttonName, expectedState),
            paginationButton.isEnabled(),
            equalTo(ENABLED.equals(expectedState))
        );
    }

    private Function<WebElement, WebElement> getNthChildDivElement(final int indexOfDivChildElement) {
        return webElement -> webElement.findElement(By.cssSelector(String.format("div:nth-child(%s)", indexOfDivChildElement)));
    }

    private Function<WebElement, String> getNthChildDivText(final int indexOfDivChildElement) {
        return webElement -> getNthChildDivElement(indexOfDivChildElement).apply(webElement).getText();
    }

    private Function<WebElement, Integer> getNthChildDivNumber(final int indexOfDivChildElement) {
        return webElement -> Integer.valueOf(getNthChildDivText(indexOfDivChildElement).apply(webElement));
    }
}
