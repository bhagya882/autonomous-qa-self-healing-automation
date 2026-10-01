package com.bhagya.qa.tests;

import com.bhagya.qa.annotations.Risk;
import com.bhagya.qa.base.BaseTest;
import com.bhagya.qa.listener.QualityListener;
import com.bhagya.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {

    @Test(description = "R001 - Valid login", groups = {"smoke", "high"})
    @Risk(Risk.Level.HIGH)
    public void validLoginTest() {
        registerTestContext("TC_LOGIN_001", "R001", "HIGH");
        LoginPage page = new LoginPage(driver);
        page.login("bhagya", "Password123");
        QualityListener.setHealing(page.getHealingStatus());
        Assert.assertEquals(page.getMessage(), "Login successful");
    }

    @Test(description = "R002 - Invalid password rejected", groups = {"regression", "medium"})
    @Risk(Risk.Level.MEDIUM)
    public void invalidPasswordTest() {
        registerTestContext("TC_LOGIN_002", "R002", "MEDIUM");
        LoginPage page = new LoginPage(driver);
        page.login("bhagya", "wrong-password");
        QualityListener.setHealing(page.getHealingStatus());
        Assert.assertEquals(page.getMessage(), "Invalid credentials");
    }
}
