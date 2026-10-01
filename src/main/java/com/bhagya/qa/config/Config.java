package com.bhagya.qa.config;

public final class Config {
    private Config() {}

    public static String appUrl() {
        String value = System.getProperty("app.url");
        return value == null || value.isBlank()
                ? "file:src/test/resources/webapp/index.html"
                : value;
    }

    public static boolean headless() {
        return Boolean.parseBoolean(System.getProperty("headless", "false"));
    }
}
