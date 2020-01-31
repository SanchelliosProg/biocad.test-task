package com.biocad.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AlternativeLoginPage extends LoginPage {

    public static final String URL = "https://account.mail.ru/";

    private static final By DOMAIN_SELECT_DIV_CSS = By.cssSelector("div.domain-select");

    public AlternativeLoginPage(WebDriver driver) {
        super(driver);
        EMAIL_INPUT_CSS = By.cssSelector("input[name='Login']");
        PASSWORD_INPUT_CSS = By.cssSelector("input[name='Password']");
        ENTER_PASSWORD_BUTTON_CSS = By.cssSelector("button[data-test-id='next-button']");
        SUBMIT_BUTTON_CSS = By.cssSelector("button[data-test-id='submit-button']");
    }

    @Override
    public LoginPage navigateTo() {
        driver.get(URL);
        return this;
    }

    @Override
    protected void chooseDomain(String domain) {
        driver.findElement(DOMAIN_SELECT_DIV_CSS).click();
        getWait(3).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div[aria-label='"+domain+"']"))).click();
    }

    @Override
    protected boolean isErrorDisplayed() {
        //p.c0126.c0126.c0145.c0138
        return getWait(3).until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("p.c0126.c0126.c0145.c0138"))).isDisplayed();
    }
}
