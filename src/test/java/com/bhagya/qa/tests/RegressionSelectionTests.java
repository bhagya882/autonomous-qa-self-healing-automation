package com.bhagya.qa.tests;

import com.bhagya.qa.analysis.ChangeImpactSelector;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

public class RegressionSelectionTests {
    @Test(description = "Select regression tests impacted by changed application files")
    public void codeChangeBasedRegressionSelection() {
        Set<String> selected = ChangeImpactSelector.selectForChangedFiles(
                List.of("LoginPage.java", "CheckoutPage.java"));
        Assert.assertTrue(selected.contains("TC_LOGIN_001"));
        Assert.assertTrue(selected.contains("TC_LOGIN_002"));
        Assert.assertTrue(selected.contains("TC_CHECKOUT_001"));
    }
}
