package com.bhagya.qa.tests;

import com.bhagya.qa.analysis.RedundancyAnalyzer;
import com.bhagya.qa.analysis.RiskBasedSelector;
import com.bhagya.qa.analysis.TestCaseGenerator;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class QualityIntelligenceTests {
    @Test(description = "Generate positive and negative test cases from a requirement")
    public void automatedTestCaseGeneration() {
        List<TestCaseGenerator.TestCase> generated =
                TestCaseGenerator.generate("R001", "Valid user should be able to log in");
        Assert.assertEquals(generated.size(), 2);
        Assert.assertEquals(generated.get(0).type(), "POSITIVE");
        Assert.assertEquals(generated.get(1).type(), "NEGATIVE");
    }

    @Test(description = "Prioritize high-risk tests before lower-risk tests")
    public void riskBasedSelection() {
        List<RiskBasedSelector.Item> result = RiskBasedSelector.prioritize(List.of(
                new RiskBasedSelector.Item("TC_LOW", "SEARCH", "LOW"),
                new RiskBasedSelector.Item("TC_HIGH", "LOGIN", "HIGH"),
                new RiskBasedSelector.Item("TC_MED", "PROFILE", "MEDIUM")
        ));
        Assert.assertEquals(result.get(0).testId(), "TC_HIGH");
        Assert.assertEquals(result.get(1).testId(), "TC_MED");
    }

    @Test(description = "Identify redundant requirement/test coverage")
    public void redundantCoverageDetection() {
        Set<String> duplicates = RedundancyAnalyzer.findDuplicateRequirements(
                new String[]{"R001", "R002", "R001", "R003", "R002"});
        Assert.assertEquals(duplicates.size(), 2);
        Assert.assertTrue(duplicates.contains("R001"));
        Assert.assertTrue(duplicates.contains("R002"));
    }
}
