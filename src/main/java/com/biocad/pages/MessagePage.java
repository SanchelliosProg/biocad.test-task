package com.biocad.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MessagePage extends Page {

    private final By THREAD_SUBJECT_CSS = By.cssSelector("h2.thread__subject");
    private final By LETTER_AUTHOR_LABEL_CSS = By.cssSelector(".letter__author span.letter__contact-item");
    private final By LETTER_CONTENT_CSS = By.cssSelector("div.js-helper.js-readmsg-msg");

    public MessagePage(WebDriver driver) {
        super(driver);
    }

    public String getTopic() {
        return new WebDriverWait(driver, 3)
                .until(ExpectedConditions.presenceOfElementLocated(THREAD_SUBJECT_CSS))
                .getText();
    }

    public String getSenderEmail() {
        return findElement(LETTER_AUTHOR_LABEL_CSS).getAttribute("title");
    }

    public String getMessageContent() {
        return findElement(LETTER_CONTENT_CSS).getText();
    }
}
