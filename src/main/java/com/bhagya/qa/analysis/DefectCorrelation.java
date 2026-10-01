package com.bhagya.qa.analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/** Correlates a failure category with known historical defect records. */
public final class DefectCorrelation {
    public record Defect(String defectId, String module, String failureType, String summary) {}

    private DefectCorrelation() {}

    public static List<Defect> findCandidates(Path csv, String failureType, String module) throws IOException {
        List<Defect> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(csv)) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] p = line.split(",", -1);
                if (p.length >= 4
                        && p[1].trim().equalsIgnoreCase(module)
                        && p[2].trim().equalsIgnoreCase(failureType)) {
                    result.add(new Defect(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()));
                }
            }
        }
        return result;
    }
}
