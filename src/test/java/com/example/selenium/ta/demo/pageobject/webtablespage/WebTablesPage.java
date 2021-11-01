package com.example.selenium.ta.demo.pageobject.webtablespage;

import com.example.selenium.ta.demo.factory.SeleniumFactory;
import com.example.selenium.ta.demo.pageobject.ParentPageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class WebTablesPage extends ParentPageObject {

    private static final String WEB_TABLES_PAGE_URL = "https://demoqa.com/webtables";

    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String EMAIL = "email";
    private static final String AGE = "age";
    private static final String SALARY = "salary";
    private static final String DEPARTMENT = "department";

    @FindBy(id = "addNewRecordButton")
    private WebElement addNewRecordButton;

    @FindBy(id = "firstName")
    private WebElement firstNameInput;

    @FindBy(id = "lastName")
    private WebElement lastNameInput;

    @FindBy(id = "userEmail")
    private WebElement emailInput;

    @FindBy(id = "age")
    private WebElement ageInput;

    @FindBy(id = "salary")
    private WebElement salaryInput;

    @FindBy(id = "department")
    private WebElement departmentInput;

    @FindBy(id = "submit")
    private WebElement submitButton;

    @FindBy(xpath = "//div[@class='rt-tr-group']/div[div[1][text()]]")
    private List<WebElement> filledTableRows;

    @FindBy(xpath = "//div[@class='action-buttons']/span[2]")
    private List<WebElement> deleteButtons;

    @FindBy(className = "pagination-bottom")
    private WebElement paginationBar;

    @FindBy(className = "rt-noData")
    private WebElement noDataTextElement;

    private final Map<String, WebElement> inputFieldsMap = Map.of(
            FIRST_NAME, firstNameInput,
            LAST_NAME, lastNameInput,
            EMAIL, emailInput,
            AGE, ageInput,
            SALARY, salaryInput,
            DEPARTMENT, departmentInput
    );

    public WebTablesPage(SeleniumFactory seleniumFactory) {
        super(seleniumFactory);
    }

    public void loadUpWebTablesPage() {
        navigateToUrl(WEB_TABLES_PAGE_URL);
    }

    public void clickOnAddNewRecordButton() {
        click(addNewRecordButton);
    }

    public WebElement getInputFieldByName(final String fieldName) {
        return inputFieldsMap.get(fieldName);
    }

    public void clickOnSubmitButton() {
        click(submitButton);
    }

    public List<WebElement> getWebTableRows() {
        return filledTableRows;
    }

    public List<WebElement> getDeleteButtons() {
        return deleteButtons;
    }

    public WebTablePagination getWebTablePagination() {
        return new WebTablePagination(getSeleniumFactory(), paginationBar);
    }

    public WebElement getNoDataTextElement() {
        return noDataTextElement;
    }

    public boolean isNoDataTextElementVisible() {
        return noDataTextElement.isDisplayed();
    }
}
