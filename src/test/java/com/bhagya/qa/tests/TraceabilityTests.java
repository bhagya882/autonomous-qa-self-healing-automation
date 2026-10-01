package com.bhagya.qa.tests;

import com.bhagya.qa.analysis.RequirementTraceability;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.List;

public class TraceabilityTests {
    private final Path requirements = Path.of("src/test/resources/data/requirements.csv");

    @Test(description = "Read requirement-to-test mappings from CSV")
    public void requirementMappingsAreLoaded() throws Exception {
        List<RequirementTraceability.Mapping> mappings = RequirementTraceability.load(requirements);
        Assert.assertEquals(mappings.size(), 4);
        Assert.assertEquals(mappings.get(0).requirementId(), "R001");
        Assert.assertEquals(mappings.get(0).testId(), "TC_LOGIN_001");
    }

    @Test(description = "Identify requirements not yet automated")
    public void uncoveredRequirementsAreDetected() throws Exception {
        List<RequirementTraceability.Mapping> mappings = RequirementTraceability.load(requirements);
        Assert.assertTrue(RequirementTraceability.uncoveredRequirements(mappings).contains("R003"));
    }
}
