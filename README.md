# Autonomous Software Quality Intelligence & Self-Healing Test Automation Platform

**Internship Project 1 – Java + Selenium + TestNG + Maven**

This repository is an internship-friendly working MVP aligned with the supplied Project 1 brief. The implementation demonstrates the requested QA intelligence capabilities while keeping the system small enough to understand and run locally.

## Covered work items

- Automated test-case generation utility (positive/negative scenarios)
- Requirement-to-test traceability using `requirements.csv` and TestNG metadata
- Risk-based test selection and prioritization
- Redundancy detection for duplicate coverage
- Flaky-test detection from historical execution behavior
- Failure classification
- Historical defect correlation by module and failure category
- Code-change based regression-test selection
- Self-healing UI locators with fallback strategies
- Automated TestNG + Extent report generation
- Automatic quality dashboard generation from execution history
- GitHub Actions CI/CD execution in headless mode

## UI demonstration

A local demo login application is included under `src/test/resources/webapp/index.html`.
The Login button intentionally has no `id=loginBtn`. `LoginPage.java` first tries that stale locator, then heals the failure using `data-testid=login-submit` or an XPath fallback.

## Run in Eclipse

1. Use JDK 17+ and Google Chrome.
2. Import as `File -> Import -> Maven -> Existing Maven Projects`.
3. Select the project folder containing `pom.xml`.
4. Right-click `testng.xml` -> `Run As -> TestNG Suite`.

Selenium 4 can resolve the Chrome driver automatically through Selenium Manager.

## Run from terminal

```bash
mvn clean test -Dheadless=true
```

## Generated outputs

- `reports/extent-report.html`
- `reports/quality-dashboard.html`
- `reports/execution-history.csv`

## Repository structure

```text
src/main/java/com/bhagya/qa
  analysis/       intelligence and selection utilities
  annotations/   risk annotation
  base/           Selenium test setup
  config/         runtime configuration
  dashboard/      dashboard generator
  healing/        self-healing element finder
  listener/       TestNG listener and reporting
  pages/          Page Object Model classes

src/test/java/com/bhagya/qa/tests
  TraceabilityTests.java
  QualityIntelligenceTests.java
  ExecutionAnalyticsTests.java
  RegressionSelectionTests.java
  DefectCorrelationTests.java
  LoginTests.java
  AdditionalLoginTests.java
  SelfHealingTests.java
```

## Scope note

The source brief describes an advanced quality platform. This repository is intentionally an internship-friendly MVP that demonstrates each requested capability without pretending to be a production-scale AI platform.
