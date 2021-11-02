package com.example.selenium.ta.demo.pageobject.hompage;

import com.example.selenium.ta.demo.factory.SeleniumFactory;
import com.example.selenium.ta.demo.pageobject.ParentPageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DemoHomePage extends ParentPageObject {

    private static final String DEMO_HOME_PAGE_URL = "https://demoqa.com/";

    @FindBy(css = "header img")
    private WebElement headerImage;

    @FindBy(css = "#app > header > a")
    private WebElement header;

    @FindBy(css = ".home-banner a")
    private WebElement joinLink;

    @FindBy(css = ".home-banner img")
    private WebElement certificationTrainingImage;

    @FindBy(className = "card")
    private List<WebElement> widgets;

    @FindBy(css = "footer")
    private WebElement footer;

    public DemoHomePage(SeleniumFactory seleniumFactory) {
        super(seleniumFactory);
    }

    public void loadUpHomePage() {
        navigateToUrl(DEMO_HOME_PAGE_URL);
    }

    public boolean isHeaderImageVisible() {
        return headerImage.isDisplayed();
    }

    public String getHeaderLink() {
        return getReferenceOfElement(header);
    }

    public String getJoinLink() {
        return getReferenceOfElement(joinLink);
    }

    public boolean isCertificationTrainingImageVisible() {
        return certificationTrainingImage.isDisplayed();
    }

    public List<WebElement> getWidgets() {
        return widgets;
    }

    public boolean isFooterVisible() {
        return footer.isDisplayed();
    }

    public WebElement getFooter() {
        return footer;
    }

}
