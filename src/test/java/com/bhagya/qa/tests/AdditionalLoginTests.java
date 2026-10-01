package com.bhagya.qa.tests;

import com.bhagya.qa.annotations.Risk;
import com.bhagya.qa.base.BaseTest;
import com.bhagya.qa.listener.QualityListener;
import com.bhagya.qa.pages.LoginPage;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AdditionalLoginTests extends BaseTest {
    @Test(description = "R003 - Missing username should not allow successful login")
    @Risk(Risk.Level.MEDIUM)
    public void missingUsernameValidation() {
        registerTestContext("TC_LOGIN_003", "R003", "MEDIUM");
        LoginPage page = new LoginPage(driver);
        page.login("", "Password123");
        QualityListener.setHealing(page.getHealingStatus());
        Assert.assertEquals(page.getMessage(), "Invalid credentials");
    }

    @Test(description = "R005 - Login form contains required controls")
    @Risk(Risk.Level.LOW)
    public void loginControlsArePresent() {
        registerTestContext("TC_LOGIN_005", "R005", "LOW");
        Assert.assertTrue(driver.findElement(By.id("username")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.id("password")).isDisplayed());
        Assert.assertTrue(driver.findElement(By.cssSelector("button[data-testid='login-submit']")).isDisplayed());
        QualityListener.setHealing("Not required");
    }
}
