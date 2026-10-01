package com.bhagya.qa.analysis;

public final class FailureClassifier {
    private FailureClassifier() {}

    public static String classify(Throwable throwable) {
        if (throwable == null) return "UNKNOWN";
        String message = throwable.toString().toLowerCase();
        if (message.contains("nosuchelement") || message.contains("unable to locate element")) {
            return "LOCATOR_FAILURE";
        }
        if (message.contains("timeout")) {
            return "TIMEOUT_FAILURE";
        }
        if (message.contains("assert")) {
            return "ASSERTION_FAILURE";
        }
        if (message.contains("connection") || message.contains("500") || message.contains("502") || message.contains("503")) {
            return "APPLICATION_OR_SERVICE_FAILURE";
        }
        return "OTHER";
    }
}
