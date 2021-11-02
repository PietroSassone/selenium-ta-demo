package com.example.selenium.ta.demo.pageobject.webtablespage;

import com.example.selenium.ta.demo.factory.SeleniumFactory;
import com.example.selenium.ta.demo.pageobject.ParentPageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class WebTablePagination extends ParentPageObject {

    @FindBy(css = ".-pageSizeOptions select")
    private WebElement resultsPerPageDropdown;

    @FindBy(css = ".-totalPages")
    private WebElement totalNumberOfTablePages;

    @FindBy(css = ".-pageJump input")
    private WebElement pageJumpField;

    @FindBy(css = ".-previous button")
    private WebElement previousPageButton;

    @FindBy(css = ".-next button")
    private WebElement nextPageButton;

    public WebTablePagination(final SeleniumFactory seleniumFactory, final WebElement parentElement) {
        super(seleniumFactory, parentElement);
    }

    public Select getResultsPerPageDropDownSelect() {
        return new Select(resultsPerPageDropdown);
    }

    public WebElement getTotalNumberOfTablePages() {
        return totalNumberOfTablePages;
    }

    public WebElement getPreviousPageButton() {
        return previousPageButton;
    }

    public WebElement getNextPageButton() {
        return nextPageButton;
    }

    public WebElement getPageJumpField() {
        return pageJumpField;
    }

    public void clickOnPreviousPaginationButton() {
        click(previousPageButton);
    }

    public void clickOnNextPaginationButton() {
        click(nextPageButton);
    }
}
