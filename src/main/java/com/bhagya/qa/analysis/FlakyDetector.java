package com.bhagya.qa.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public final class FlakyDetector {
    private FlakyDetector() {}

    public static Map<String, Boolean> analyze(Path csv) throws IOException {
        Map<String, String> state = new HashMap<>();
        Map<String, Boolean> flaky = new HashMap<>();
        if (!Files.exists(csv)) return flaky;

        try (BufferedReader reader = Files.newBufferedReader(csv)) {
            String line;
            reader.readLine(); // header
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 2) continue;
                String testId = parts[0].trim();
                String status = parts[1].trim();
                String previous = state.put(testId, status);
                if (previous != null && !previous.equalsIgnoreCase(status)) {
                    flaky.put(testId, true);
                }
            }
        }
        return flaky;
    }
}
