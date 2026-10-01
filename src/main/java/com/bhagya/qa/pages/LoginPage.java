package com.bhagya.qa.pages;

import com.bhagya.qa.healing.SelfHealingElementFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginPage {
    private final WebDriver driver;
    private final SelfHealingElementFinder healer;

    private final By username = By.id("username");
    private final By password = By.id("password");

    // Intentionally stale primary locator to demonstrate self-healing.
    private final By staleLoginButton = By.id("loginBtn");
    private final By fallbackLoginButton = By.cssSelector("button[data-testid='login-submit']");
    private final By fallbackByText = By.xpath("//button[normalize-space()='Login']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.healer = new SelfHealingElementFinder(driver);
    }

    public void login(String user, String pass) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(pass);
        WebElement login = healer.find("Login button", staleLoginButton,
                fallbackLoginButton, fallbackByText);
        login.click();
    }

    public String getHealingStatus() {
        return healer.getLastHealingMessage();
    }

    public String getMessage() {
        return driver.findElement(By.id("message")).getText();
    }
}
