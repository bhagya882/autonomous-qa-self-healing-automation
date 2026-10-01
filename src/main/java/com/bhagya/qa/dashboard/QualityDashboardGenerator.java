package com.bhagya.qa.dashboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class QualityDashboardGenerator {

    private QualityDashboardGenerator() {
    }

    public static void generate(
            Path history,
            Path latestRun,
            Path output) throws IOException {

        List<String[]> historyRecords =
                readRecords(history);

        List<String[]> latestRecords =
                readRecords(latestRun);

        Stats historicalStats =
                calculateStats(historyRecords);

        Stats latestStats =
                calculateStats(latestRecords);

        double latestPassRate =
                latestStats.total == 0
                        ? 0
                        : (latestStats.passed * 100.0 / latestStats.total);

        double historicalPassRate =
                historicalStats.total == 0
                        ? 0
                        : (historicalStats.passed * 100.0 /
                           historicalStats.total);

        String latestHealth =
                latestStats.failed == 0
                        ? "GREEN / HEALTHY"
                        : "ATTENTION REQUIRED";

        String html = """
                <!DOCTYPE html>

                <html>

                <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width, initial-scale=1">

                    <title>
                        QA Quality Dashboard
                    </title>

                    <style>

                        body {
                            font-family: Arial, sans-serif;
                            margin: 40px;
                            background: #f6f7fb;
                            color: #111;
                        }

                        h1 {
                            margin-bottom: 8px;
                        }

                        h2 {
                            margin-top: 32px;
                        }

                        .subtitle {
                            color: #555;
                        }

                        .grid {
                            display: grid;
                            grid-template-columns:
                                repeat(5, minmax(140px, 1fr));
                            gap: 16px;
                        }

                        .card {
                            background: white;
                            padding: 20px;
                            border-radius: 12px;
                            box-shadow:
                                0 2px 10px #00000012;
                        }

                        .label {
                            font-size: 16px;
                        }

                        .value {
                            font-size: 30px;
                            font-weight: bold;
                            margin-top: 8px;
                        }

                        table {
                            width: 100%%;
                            border-collapse: collapse;
                            background: white;
                            margin-top: 24px;
                        }

                        th,
                        td {
                            padding: 10px;
                            border: 1px solid #ddd;
                            text-align: left;
                        }

                        th {
                            font-weight: bold;
                        }

                        .healthy {
                            font-weight: bold;
                        }

                        .note {
                            color: #555;
                            margin-top: 12px;
                        }

                        @media (max-width: 900px) {

                            .grid {
                                grid-template-columns:
                                    repeat(2, 1fr);
                            }

                            body {
                                margin: 20px;
                            }
                        }

                    </style>

                </head>

                <body>

                    <h1>
                        Autonomous QA Quality Dashboard
                    </h1>

                    <p class="subtitle">
                        Generated automatically from
                        TestNG execution history.
                    </p>

                    <h2>
                        Latest Test Run
                    </h2>

                    <div class="grid">

                        <div class="card">

                            <div class="label">
                                Total Tests
                            </div>

                            <div class="value">
                                %d
                            </div>

                        </div>

                        <div class="card">

                            <div class="label">
                                Passed
                            </div>

                            <div class="value">
                                %d
                            </div>

                        </div>

                        <div class="card">

                            <div class="label">
                                Failed
                            </div>

                            <div class="value">
                                %d
                            </div>

                        </div>

                        <div class="card">

                            <div class="label">
                                Pass %%
                            </div>

                            <div class="value">
                                %.2f
                            </div>

                        </div>

                        <div class="card">

                            <div class="label">
                                Healed
                            </div>

                            <div class="value">
                                %d
                            </div>

                        </div>

                    </div>

                    <table>

                        <tr>

                            <th>
                                Latest Run Metric
                            </th>

                            <th>
                                Value
                            </th>

                        </tr>

                        <tr>

                            <td>
                                Test health
                            </td>

                            <td class="healthy">
                                %s
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Failure count
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Skipped
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Self-healing events
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Requirement traceability
                            </td>

                            <td>
                                See
                                src/test/resources/data/requirements.csv
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Risk selection
                            </td>

                            <td>
                                HIGH → MEDIUM → LOW
                            </td>

                        </tr>

                    </table>

                    <h2>
                        Historical Test Executions
                    </h2>

                    <table>

                        <tr>

                            <th>
                                Metric
                            </th>

                            <th>
                                Value
                            </th>

                        </tr>

                        <tr>

                            <td>
                                Total Historical Executions
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Total Passed
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Total Failed
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Total Skipped
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Total Self-healing Events
                            </td>

                            <td>
                                %d
                            </td>

                        </tr>

                        <tr>

                            <td>
                                Historical Pass Rate
                            </td>

                            <td>
                                %.2f%%
                            </td>

                        </tr>

                    </table>

                    <p class="note">
                        Latest-run metrics are calculated from
                        reports/latest-run.csv.
                    </p>

                </body>

                </html>
                """.formatted(

                // Latest run
                latestStats.total,
                latestStats.passed,
                latestStats.failed,
                latestPassRate,
                latestStats.healed,

                // Latest metrics
                latestHealth,
                latestStats.failed,
                latestStats.skipped,
                latestStats.healed,

                // Historical metrics
                historicalStats.total,
                historicalStats.passed,
                historicalStats.failed,
                historicalStats.skipped,
                historicalStats.healed,
                historicalPassRate
        );

        Files.createDirectories(
                output.getParent()
        );

        Files.writeString(
                output,
                html
        );
    }

    private static List<String[]> readRecords(
            Path file) throws IOException {

        List<String[]> records =
                new ArrayList<>();

        if (!Files.exists(file)) {
            return records;
        }

        List<String> lines =
                Files.readAllLines(file);

        for (int i = 1; i < lines.size(); i++) {

            String line = lines.get(i);

            if (line == null ||
                line.isBlank()) {

                continue;
            }

            List<String> fields =
                    parseCsvLine(line);

            if (fields.size() < 7) {
                continue;
            }

            String[] record =
                    new String[fields.size()];

            for (int j = 0;
                 j < fields.size();
                 j++) {

                record[j] =
                        fields.get(j).trim();
            }

            records.add(record);
        }

        return records;
    }

    /*
     * Simple CSV parser that correctly handles quoted values.
     */
    private static List<String> parseCsvLine(
            String line) {

        List<String> values =
                new ArrayList<>();

        StringBuilder current =
                new StringBuilder();

        boolean insideQuotes = false;

        for (int i = 0;
             i < line.length();
             i++) {

            char ch = line.charAt(i);

            if (ch == '"') {

                if (insideQuotes &&
                    i + 1 < line.length() &&
                    line.charAt(i + 1) == '"') {

                    current.append('"');
                    i++;

                } else {

                    insideQuotes =
                            !insideQuotes;
                }

            } else if (ch == ',' &&
                       !insideQuotes) {

                values.add(
                        current.toString()
                );

                current.setLength(0);

            } else {

                current.append(ch);
            }
        }

        values.add(
                current.toString()
        );

        return values;
    }

    private static Stats calculateStats(
            List<String[]> records) {

        int total = 0;
        int passed = 0;
        int failed = 0;
        int skipped = 0;
        int healed = 0;

        for (String[] record : records) {

            if (record.length < 7) {
                continue;
            }

            total++;

            String status =
                    record[1]
                            .trim()
                            .toUpperCase();

            switch (status) {

                case "PASS" -> passed++;

                case "FAIL" -> failed++;

                case "SKIP" -> skipped++;

                default -> skipped++;
            }

            String healingValue =
                    record[6]
                            .trim()
                            .toUpperCase();

            if (healingValue.contains("HEALED:")) {
                healed++;
            }
        }

        return new Stats(
                total,
                passed,
                failed,
                skipped,
                healed
        );
    }

    private record Stats(
            int total,
            int passed,
            int failed,
            int skipped,
            int healed) {
    }
}