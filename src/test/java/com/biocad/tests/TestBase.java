package com.biocad.tests;

import com.biocad.models.User;
import com.biocad.pages.MailPage;
import com.biocad.utils.TargetAccountsContext;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class TestBase {

    private static Logger log = LogManager.getLogger(TestBase.class);

    public static WebDriver driver;

    protected static User targetUser;
    protected static User senderUser;

    @BeforeAll
    public static void setup() throws ParseException, IOException {

        if (Boolean.parseBoolean(System.getProperty("loadWebDriverFromTestProject"))) {
            log.debug("Getting chromedriver from local project");
            if (SystemUtils.IS_OS_WINDOWS) {
                log.debug("Loading Chrome Driver for Windows");
                System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
            } else if (SystemUtils.IS_OS_LINUX) {
                log.debug("Loading Chrome Driver for Linux");
                System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriverlinux");
            } else {
                log.debug("Loading Chrome Driver for OS X");
                System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver");
            }
        }

        log.debug("Starting Chrome Driver");
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        JSONObject testDataJson;
        try {
            String pathToTestDataFile = System.getProperty("testDataPath");
            testDataJson = (JSONObject) new JSONParser().parse(new FileReader(pathToTestDataFile));
            log.debug("Successfully loaded test-data file from {}", pathToTestDataFile);
        } catch (NullPointerException ex) {
            log.warn("File with test data could not be found in properties. Going to try load from default directory");
            testDataJson = (JSONObject) new JSONParser().parse(new FileReader("./src/test/resources/test-data.json"));
        }

        senderUser = new User((Map<String, String>) testDataJson.get("sender"));
        targetUser = new User((Map<String, String>) testDataJson.get("target"));

        log.info("Sender user: username - {}", senderUser.getUsername());
        log.info("Target user: username - {}", targetUser.getUsername());
    }

    @AfterAll
    public static void tearDown() {
        if (TargetAccountsContext.getInstance().isTargetUserLoggedIn()) {
            cleanupTargetAccount();
        }
        driver.close();
        log.info("Web driver was closed");
    }

    protected static void cleanupTargetAccount() {
        MailPage mailPage = new MailPage(driver);
        mailPage.deleteAllMails();
        mailPage.logout();
    }
}
