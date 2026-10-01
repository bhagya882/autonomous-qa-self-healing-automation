package com.bhagya.qa.tests;

import com.bhagya.qa.annotations.Risk;
import com.bhagya.qa.base.BaseTest;
import com.bhagya.qa.listener.QualityListener;
import com.bhagya.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SelfHealingTests extends BaseTest {
    @Test(description = "Heal a stale primary locator using a fallback locator")
    @Risk(Risk.Level.HIGH)
    public void staleLoginLocatorIsHealed() {
        registerTestContext("TC_HEAL_001", "R004", "HIGH");
        LoginPage page = new LoginPage(driver);
        page.login("bhagya", "Password123");
        String status = page.getHealingStatus();
        QualityListener.setHealing(status);
        Assert.assertTrue(status.startsWith("HEALED:"), "Expected the stale locator to be healed");
        Assert.assertEquals(page.getMessage(), "Login successful");
    }
}
