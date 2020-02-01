package com.biocad.pages;

import com.biocad.models.User;
import com.biocad.utils.TargetAccountsContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends Page {

    private Logger log = LogManager.getLogger(getClass());

    private final String URL = "https://mail.ru/";

    protected By loginError;
    protected By passwordError;

    protected By EMAIL_INPUT_CSS = By.cssSelector("#mailbox\\:login");
    protected By PASSWORD_INPUT_CSS = By.cssSelector("#mailbox\\:password");
    protected By ENTER_PASSWORD_BUTTON_CSS = By.cssSelector("#mailbox\\:submit > input");
    protected By SUBMIT_BUTTON_CSS = By.cssSelector("#mailbox\\:submit > input");

    private final By LOGGED_IN_USER_LABEL_CSS = By.cssSelector("#mailbox\\:dropdown_button > div");
    private final By DOMAIN_SELECT = By.cssSelector("select#mailbox\\:domain");

    public LoginPage(WebDriver driver) {
        super(driver);
        By mailboxErrorCss = By.cssSelector("div#mailbox\\:error");
        loginError = mailboxErrorCss;
        passwordError = mailboxErrorCss;
    }

    public LoginPage navigateTo() {
        driver.get(URL);
        return this;
    }

    public MailPage login(User user) {
        TargetAccountsContext targetAccountsContext = TargetAccountsContext.getInstance();
        try {
            WebElement userIcon = getWait(3).until(ExpectedConditions.presenceOfElementLocated(LOGGED_IN_USER_LABEL_CSS));
            log.info("User {} is already logged in - going to Mail Page directly", user.toString());
            userIcon.click();
            return new MailPage(driver);
        } catch (TimeoutException ex) {
            log.info("User is not logged in - signing in");
            targetAccountsContext.isTargetUserLoggedIn(false);
            if (isCurrentPageAlternativeLogin()) {
                log.warn("Something went wrong and we were redirected to {}, - trying to login through this page.", AlternativeLoginPage.URL);
                AlternativeLoginPage page = new AlternativeLoginPage(driver);
                page.chooseDomain(user.getDomain());
                page.enterLogin(user.getUsername());
                page.enterPassword(user.getPassword());
            } else {
                chooseDomain(user.getDomain());
                enterLogin(user.getUsername());
                enterPassword(user.getPassword());
            }

            if (isCurrentPageAlternativeLogin()) {
                throw new IllegalArgumentException("Failed to login user: " + user.toString());
            } else {
                targetAccountsContext.isTargetUserLoggedIn(true);
                return new MailPage(driver);
            }
        }
    }

    protected void chooseDomain(String domain) {
        Select select = new Select(driver.findElement(DOMAIN_SELECT));
        select.selectByVisibleText(domain);
    }

    protected void enterLogin(String login) {
        activateInputField(EMAIL_INPUT_CSS).sendKeys(login);
        driver.findElement(ENTER_PASSWORD_BUTTON_CSS).click();
        if(isErrorDisplayed(loginError)){
            throw new IllegalArgumentException("Failed to login user. Wrong mail name");
        } else {
            log.info("Mail name was found in the system.");
        }
    }

    protected void enterPassword(String password) {
        activateInputField(PASSWORD_INPUT_CSS).sendKeys(password);
        driver.findElement(SUBMIT_BUTTON_CSS).click();
        if(isErrorDisplayed(passwordError)){
            throw new IllegalArgumentException("Failed to login user. Wrong password");
        } else {
            log.info("Successfully logged in");
        }
    }

    protected boolean isErrorDisplayed(By selector) {
        try {
            return getWait(negativeWaitTimeout).until(ExpectedConditions.presenceOfElementLocated(selector)).isDisplayed();
        } catch (TimeoutException ex) {
            log.info("No error message detected while logging in");
            return false;
        }
    }

    protected WebDriverWait getWait(long timeout) {
        return new WebDriverWait(driver, timeout);
    }

    private WebElement activateInputField(By selector) {
        WebElement element = getWait(webElementTimeout).until(ExpectedConditions.elementToBeClickable(selector));
        element.click();
        return element;
    }


    private boolean isCurrentPageAlternativeLogin() {
        return driver.getCurrentUrl().contains(AlternativeLoginPage.URL);
    }
}
