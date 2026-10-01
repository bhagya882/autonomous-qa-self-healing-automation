package com.bhagya.qa.analysis;

import java.util.ArrayList;
import java.util.List;

public final class TestCaseGenerator {
    private TestCaseGenerator() {}

    public record TestCase(String id, String requirementId, String scenario, String type) {}

    public static List<TestCase> generate(String requirementId, String description) {
        List<TestCase> cases = new ArrayList<>();
        cases.add(new TestCase(requirementId + "_POS", requirementId,
                "Validate expected behavior: " + description, "POSITIVE"));
        cases.add(new TestCase(requirementId + "_NEG", requirementId,
                "Validate rejection/error behavior: " + description, "NEGATIVE"));
        return cases;
    }
}
