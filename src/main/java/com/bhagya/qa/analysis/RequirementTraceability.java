package com.bhagya.qa.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Loads requirement-to-test mappings from the project CSV. */
public final class RequirementTraceability {
    public record Mapping(String requirementId, String description, String module,
                           String risk, String testId, String automation) {}

    private RequirementTraceability() {}

    public static List<Mapping> load(Path csv) throws IOException {
        List<Mapping> mappings = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(csv)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 6) {
                    mappings.add(new Mapping(p[0].trim(), p[1].trim(), p[2].trim(),
                            p[3].trim(), p[4].trim(), p[5].trim()));
                }
            }
        }
        return mappings;
    }

    public static Set<String> uncoveredRequirements(List<Mapping> mappings) {
        Set<String> uncovered = new HashSet<>();
        for (Mapping mapping : mappings) {
            if (!"Yes".equalsIgnoreCase(mapping.automation())) {
                uncovered.add(mapping.requirementId());
            }
        }
        return uncovered;
    }
}
