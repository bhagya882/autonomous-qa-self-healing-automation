package com.bhagya.qa.listener;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.bhagya.qa.analysis.FailureClassifier;
import com.bhagya.qa.dashboard.QualityDashboardGenerator;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

public class QualityListener implements ITestListener {

    private static final ThreadLocal<String> testId =
            ThreadLocal.withInitial(() -> "UNMAPPED");

    private static final ThreadLocal<String> requirementId =
            ThreadLocal.withInitial(() -> "UNMAPPED");

    private static final ThreadLocal<String> risk =
            ThreadLocal.withInitial(() -> "MEDIUM");

    private static final ThreadLocal<String> healing =
            ThreadLocal.withInitial(() -> "Not recorded");

    private static ExtentReports extent;

    /*
     * This file contains ALL historical test executions.
     */
    private static final Path HISTORY =
            Path.of("reports/execution-history.csv");

    /*
     * This file contains ONLY the current TestNG run.
     * It removes the ambiguity caused by counting TestNG contexts.
     */
    private static final Path LATEST_RUN =
            Path.of("reports/latest-run.csv");

    private static final Path EXTENT =
            Path.of("reports/extent-report.html");

    private static final Path DASHBOARD =
            Path.of("reports/quality-dashboard.html");

    /*
     * Prevent multiple TestNG contexts from wiping the same latest-run file.
     */
    private static boolean latestRunInitialized = false;

    public static void setCurrentMetadata(
            String id,
            String req,
            String riskValue) {

        testId.set(id);
        requirementId.set(req);
        risk.set(riskValue);
    }

    public static void setHealing(String message) {
        healing.set(message);
    }

    @Override
    public void onStart(ITestContext context) {

        try {

            Files.createDirectories(HISTORY.getParent());

            /*
             * Create historical file only when it does not exist.
             */
            if (!Files.exists(HISTORY)) {
                Files.writeString(
                        HISTORY,
                        "testId,status,requirementId,risk,failureType,timestamp,healing\n"
                );
            }

            /*
             * Create/truncate latest-run file only once
             * for the complete TestNG execution.
             */
            if (!latestRunInitialized) {

                Files.writeString(
                        LATEST_RUN,
                        "testId,status,requirementId,risk,failureType,timestamp,healing\n",
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING
                );

                latestRunInitialized = true;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ExtentSparkReporter reporter =
                new ExtentSparkReporter(EXTENT.toFile());

        extent = new ExtentReports();
        extent.attachReporter(reporter);
    }

    private void record(
            ITestResult result,
            String status,
            String failureType) {

        String line = String.join(
                ",",
                csv(testId.get()),
                csv(status),
                csv(requirementId.get()),
                csv(risk.get()),
                csv(failureType),
                csv(LocalDateTime.now().toString()),
                csv(healing.get())
        ) + "\n";

        try {

            /*
             * 1. Store permanently in historical execution file.
             */
            Files.writeString(
                    HISTORY,
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

            /*
             * 2. Store in current-run file.
             */
            Files.writeString(
                    LATEST_RUN,
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String csv(String value) {

        return "\"" +
                String.valueOf(value)
                        .replace("\"", "\"\"")
                + "\"";
    }

    @Override
    public void onTestStart(ITestResult result) {

        /*
         * Reset healing state for every test.
         */
        healing.set("Not recorded");

        ExtentTest test =
                extent.createTest(
                        result.getMethod().getMethodName()
                );

        result.setAttribute("extentTest", test);

        test.info(
                "Requirement: " +
                requirementId.get()
        );

        test.info(
                "Risk: " +
                risk.get()
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        record(
                result,
                "PASS",
                "NONE"
        );

        ExtentTest test =
                (ExtentTest) result.getAttribute("extentTest");

        if (test != null) {

            test.pass(
                    "Test passed. " +
                    healing.get()
            );
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {

        String type =
                FailureClassifier.classify(
                        result.getThrowable()
                );

        record(
                result,
                "FAIL",
                type
        );

        ExtentTest test =
                (ExtentTest) result.getAttribute("extentTest");

        if (test != null) {

            test.fail(
                    type +
                    ": " +
                    result.getThrowable()
            );
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {

        record(
                result,
                "SKIP",
                "SKIPPED"
        );

        ExtentTest test =
                (ExtentTest) result.getAttribute("extentTest");

        if (test != null) {

            test.skip("Skipped");
        }
    }

    @Override
    public void onFinish(ITestContext context) {

        if (extent != null) {
            extent.flush();
        }

        try {

            /*
             * Dashboard reads latest-run.csv directly,
             * so it no longer depends on TestNG context counts.
             */
            QualityDashboardGenerator.generate(
                    HISTORY,
                    LATEST_RUN,
                    DASHBOARD
            );

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }
}