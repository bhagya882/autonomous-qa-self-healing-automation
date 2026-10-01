package com.bhagya.qa.base;

import com.bhagya.qa.config.Config;
import com.bhagya.qa.listener.QualityListener;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.nio.file.Path;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (Config.headless()) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--window-size=1440,900");
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options); // Selenium Manager resolves the driver.
        driver.manage().window().maximize();
        String appUrl = System.getProperty("app.url",
                Path.of("src/test/resources/webapp/index.html").toAbsolutePath().toUri().toString());
        driver.get(appUrl);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void registerTestContext(String testId, String requirementId, String risk) {
        QualityListener.setCurrentMetadata(testId, requirementId, risk);
    }
}
