package com.biocad.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static java.lang.System.*;

public abstract class Page {
    protected WebDriver driver;

    protected int webElementTimeout;
    protected int negativeWaitTimeout;

    public Page(WebDriver driver) {
        this.driver = driver;

        try {
            webElementTimeout = Integer.parseInt(getProperty("webElementTimeout"));
        } catch (NullPointerException | NumberFormatException e) {
            webElementTimeout = 3;
        }

        try {
            negativeWaitTimeout = Integer.parseInt(getProperty("negativeWaitTimeout"));
        } catch (NullPointerException | NumberFormatException e) {
            negativeWaitTimeout = 1;
        }
    }

    protected WebElement findElement(By selector) {
        return driver.findElement(selector);
    }
}
