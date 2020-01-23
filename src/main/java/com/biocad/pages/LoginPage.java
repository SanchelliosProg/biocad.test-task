package com.biocad.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends Page {
    private final String URL = "https://mail.ru/";

    private final By EMAIL_INPUT_CSS = By.cssSelector("#mailbox\\:login");
    private final By PASSWORD_INPUT_CSS = By.cssSelector("#mailbox\\:password");
    private final By ENTER_PASSWORD_BUTTON_CSS = By.cssSelector("#mailbox\\:submit > input");
    private final By SUBMIT_BUTTON_CSS = By.cssSelector("#mailbox\\:submit > input");
    private final By LOGGED_IN_USER_LABEL_CSS = By.cssSelector("#mailbox\\:dropdown_button > div");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage navigateTo() {
        driver.get(URL);
        return this;
    }

    public MailPage login(String login, String password) {
        try {
            WebElement userIcon = getWait(3).until(ExpectedConditions.presenceOfElementLocated(LOGGED_IN_USER_LABEL_CSS));
            userIcon.click();
            return new MailPage(driver);
        } catch (TimeoutException ex) {
            activateInputField(EMAIL_INPUT_CSS).sendKeys(login);
            driver.findElement(ENTER_PASSWORD_BUTTON_CSS).click();
            activateInputField(PASSWORD_INPUT_CSS).sendKeys(password);
            driver.findElement(SUBMIT_BUTTON_CSS).click();
            return new MailPage(driver);
        }
    }

    private WebElement activateInputField(By selector) {
        WebElement element = getWait(3).until(ExpectedConditions.elementToBeClickable(selector));
        element.click();
        return element;
    }

    private WebDriverWait getWait(long timeout) {
        return new WebDriverWait(driver, timeout);
    }
}
