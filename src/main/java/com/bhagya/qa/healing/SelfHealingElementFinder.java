package com.bhagya.qa.healing;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Arrays;

public class SelfHealingElementFinder {
    private final WebDriver driver;
    private final Duration wait = Duration.ofSeconds(5);
    private volatile String lastHealingMessage = "Not required";

    public SelfHealingElementFinder(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement find(String elementName, By primary, By... fallbacks) {
        try {
            WebElement element = driver.findElement(primary);
            lastHealingMessage = "Not required (primary locator succeeded)";
            return element;
        } catch (Exception ignored) {
            // Continue to fallback locators.
        }

        for (By fallback : fallbacks) {
            try {
                WebElement element = driver.findElement(fallback);
                lastHealingMessage = "HEALED: " + elementName + " using fallback " + fallback;
                return element;
            } catch (Exception ignored) {
                // Try next candidate.
            }
        }

        lastHealingMessage = "Healing failed for " + elementName + "; attempted "
                + Arrays.toString(fallbacks);
        throw new NoSuchElementException(lastHealingMessage);
    }

    public String getLastHealingMessage() {
        return lastHealingMessage;
    }
}
