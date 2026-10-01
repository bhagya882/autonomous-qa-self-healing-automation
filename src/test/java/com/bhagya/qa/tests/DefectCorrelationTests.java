package com.bhagya.qa.tests;

import com.bhagya.qa.analysis.DefectCorrelation;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.util.List;

public class DefectCorrelationTests {
    @Test(description = "Correlate failure category with historical defects")
    public void historicalDefectCorrelation() throws Exception {
        List<DefectCorrelation.Defect> candidates = DefectCorrelation.findCandidates(
                Path.of("src/test/resources/data/defects.csv"),
                "LOCATOR_FAILURE", "LOGIN");
        Assert.assertEquals(candidates.size(), 1);
        Assert.assertEquals(candidates.get(0).defectId(), "D001");
    }
}
