package com.biocad.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MailPage extends Page {
    private final By LIST_OF_MESSAGES_CSS = By.cssSelector("div.dataset.dataset_select-mode_off");

    public MailPage(WebDriver driver) {
        super(driver);
    }

    public MessagePage openMessageWithTitle(String text) {
        new WebDriverWait(driver, 3).until(ExpectedConditions.presenceOfElementLocated(LIST_OF_MESSAGES_CSS));
        try {
            driver.findElement(By.xpath("//span[text() = '" + text + "']")).click();
        } catch (NoSuchElementException e) {
            return new MessagePage(driver, false);
        }
        return new MessagePage(driver);
    }
}
