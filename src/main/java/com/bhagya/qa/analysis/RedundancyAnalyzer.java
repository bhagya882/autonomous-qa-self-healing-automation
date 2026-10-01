package com.bhagya.qa.analysis;

import java.util.HashSet;
import java.util.Set;

public final class RedundancyAnalyzer {
    private RedundancyAnalyzer() {}

    public static Set<String> findDuplicateRequirements(String[] requirementIds) {
        Set<String> seen = new HashSet<>();
        Set<String> duplicates = new HashSet<>();
        for (String id : requirementIds) {
            if (!seen.add(id)) duplicates.add(id);
        }
        return duplicates;
    }
}
