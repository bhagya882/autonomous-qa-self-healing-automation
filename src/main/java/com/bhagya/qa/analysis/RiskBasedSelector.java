package com.bhagya.qa.analysis;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class RiskBasedSelector {
    public record Item(String testId, String module, String risk) {}

    private RiskBasedSelector() {}

    public static List<Item> prioritize(List<Item> tests) {
        List<Item> copy = new ArrayList<>(tests);
        copy.sort(Comparator.comparingInt(item -> switch (item.risk().toUpperCase()) {
            case "HIGH" -> 1;
            case "MEDIUM" -> 2;
            default -> 3;
        }));
        return copy;
    }
}
