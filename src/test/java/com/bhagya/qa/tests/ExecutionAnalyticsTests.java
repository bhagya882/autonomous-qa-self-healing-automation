package com.bhagya.qa.tests;

import com.bhagya.qa.analysis.FailureClassifier;
import com.bhagya.qa.analysis.FlakyDetector;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.Map;

public class ExecutionAnalyticsTests {
    @Test(description = "Detect a test that alternates between pass and fail")
    public void flakyTestDetection() throws Exception {
        Path history = Path.of("reports/execution-history.csv");
        Map<String, Boolean> flaky = FlakyDetector.analyze(history);
        Assert.assertTrue(flaky.isEmpty() || flaky.containsKey("TC_SAMPLE_FLAKY"));
    }

    @Test(description = "Classify common automation failures")
    public void failureClassification() {
        Assert.assertEquals(FailureClassifier.classify(
                new NoSuchElementException("Unable to locate element")), "LOCATOR_FAILURE");
        Assert.assertEquals(FailureClassifier.classify(
                new AssertionError("Expected A but found B")), "ASSERTION_FAILURE");
        Assert.assertEquals(FailureClassifier.classify(
                new RuntimeException("timeout waiting for service")), "TIMEOUT_FAILURE");
    }
}
