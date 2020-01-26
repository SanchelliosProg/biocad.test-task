package com.biocad.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class MailPage extends Page {
    private final String URL = "https://e.mail.ru/inbox/";

    private final By SECTION_WITH_MESSAGES_CSS = By.cssSelector("div.dataset.dataset_select-mode_off");
    private final By LIST_OF_MESSAGES_CSS = By.cssSelector("div.dataset.dataset_select-mode_off > div > a");
    private final By DELETE_BUTTON_CSS = By.cssSelector("body > div.contextmenu.contextmenu_expanded > div > div > div > div > div:nth-child(4) > span.list-item__text");


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
            return new MessagePage(driver, false);
        }
        return new MessagePage(driver);
    }

    public void deleteAllMails() {
        driver.get(URL);
        waitForInboxMessagesToAppear();
        List<WebElement> messages = driver.findElements(LIST_OF_MESSAGES_CSS);
        messages.remove(messages.size()-1);
        messages.forEach(e -> {
            Actions a = new Actions(driver);
            a.moveToElement(e).contextClick().perform();
            driver.findElement(DELETE_BUTTON_CSS).click();
        });
    }

    private void waitForInboxMessagesToAppear() {
        new WebDriverWait(driver, 3).until(ExpectedConditions.presenceOfElementLocated(SECTION_WITH_MESSAGES_CSS));
    }
}
