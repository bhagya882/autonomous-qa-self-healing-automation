package com.bhagya.qa.analysis;

import java.util.*;

public final class ChangeImpactSelector {
    private ChangeImpactSelector() {}

    private static final Map<String, String> MODULE_BY_FILE = Map.of(
            "LoginPage.java", "LOGIN",
            "HomePage.java", "HOME",
            "CartPage.java", "CART",
            "CheckoutPage.java", "CHECKOUT"
    );

    private static final Map<String, List<String>> TESTS_BY_MODULE = Map.of(
            "LOGIN", List.of("TC_LOGIN_001", "TC_LOGIN_002"),
            "HOME", List.of("TC_HOME_001"),
            "CART", List.of("TC_CART_001"),
            "CHECKOUT", List.of("TC_CHECKOUT_001")
    );

    public static Set<String> selectForChangedFiles(List<String> changedFiles) {
        Set<String> selected = new LinkedHashSet<>();
        for (String file : changedFiles) {
            String module = MODULE_BY_FILE.get(file);
            if (module != null) selected.addAll(TESTS_BY_MODULE.getOrDefault(module, List.of()));
        }
        return selected;
    }
}
