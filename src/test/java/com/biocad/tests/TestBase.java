package com.biocad.tests;

import org.apache.commons.lang3.SystemUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileReader;
import java.io.IOException;

public class TestBase {

    public static WebDriver driver;
    protected static String username;
    protected static String password;

    @BeforeAll
    public static void setup() throws ParseException, IOException {

        if (Boolean.parseBoolean(System.getProperty("loadWebDriverFromTestProject"))) {
            System.out.println("Getting chromedriver from local project");
            if(SystemUtils.IS_OS_WINDOWS) {
                System.out.println("WIN");
                System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
            } else if (SystemUtils.IS_OS_LINUX) {
                System.out.println("LINUX");
                System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriverlinux");
            } else {
                System.out.println("MAC");
                System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver");
            }
        }

        driver = new ChromeDriver();
        driver.manage().window().maximize();

        JSONObject testDataJson;
        try {
            testDataJson = (JSONObject) new JSONParser().parse(new FileReader(System.getProperty("testDataPath")));
        } catch (NullPointerException ex) {
            System.out.println("File with test data could not be found in properties. Going to try load from default directory");
            testDataJson = (JSONObject) new JSONParser().parse(new FileReader("./src/test/resources/test-data.json"));
        }

        username = (String) testDataJson.get("username");
        password = (String) testDataJson.get("password");
    }

    @AfterAll
    public static void tearDown() {
        driver.close();
    }


}
