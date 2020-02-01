package com.biocad.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MailPage extends Page {
    private Logger log = LogManager.getLogger(getClass());

    private final String URL = "https://e.mail.ru/inbox/";

    private final By SECTION_WITH_MESSAGES_CSS = By.cssSelector("div.dataset.dataset_select-mode_off");
    private final By LIST_OF_MESSAGES_CSS = By.cssSelector("div.dataset.dataset_select-mode_off > div > a");
    private final By DELETE_BUTTON_CSS = By.cssSelector("body > div.contextmenu.contextmenu_expanded > div > div > div > div > div:nth-child(4) > span.list-item__text");
    private final By LOGOUT_BUTTON_CSS = By.cssSelector("a[title='выход']");

    public MailPage(WebDriver driver) {
        super(driver);
    }

    public MessagePage openMessageWithTitle(String text) {
        waitForInboxMessagesToAppear();
        try {
            WebElement messageSpan = driver.findElement(By.xpath("//span[text() = '" + text + "']"));
            new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(messageSpan));
            messageSpan.click();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Message with text: " + text + " in the title was not found. Test process is stopped.");
        }
        return new MessagePage(driver);
    }

    public void deleteAllMails() {
        driver.get(URL);
        waitForInboxMessagesToAppear();
        List<WebElement> messages = driver.findElements(LIST_OF_MESSAGES_CSS);
        if (messages.size() > 0) {
            messages.remove(messages.size()-1);
            messages.forEach(e -> {
                Actions a = new Actions(driver);
                a.moveToElement(e).contextClick().perform();
                driver.findElement(DELETE_BUTTON_CSS).click();
            });
            log.info("Mails are deleted");
        } else {
            log.warn("Inbox was empty");
        }
    }

    public void logout() {
        driver.findElement(LOGOUT_BUTTON_CSS).click();
    }

    private void waitForInboxMessagesToAppear() {
        new WebDriverWait(driver, webElementTimeout).until(ExpectedConditions.presenceOfElementLocated(SECTION_WITH_MESSAGES_CSS));
    }
}
