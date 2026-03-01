# LinkedIn Jobs Automation

## Overview
This project is a Selenium + TestNG automation framework for LinkedIn Jobs flow.

## Tech Stack
- Java 17
- Maven
- Selenium WebDriver
- TestNG
- Allure Report

## Project Structure
- `src/main/java`: page objects, locators, shared framework code
- `src/test/java`: test classes, base test, listeners, test data providers
- `src/test/resources/runTest`: TestNG suite XML files
- `src/test/resources/environmentConfig`: environment properties
- `src/test/resources/uploadFiles`: files used for upload test cases

## Prerequisites
1. Install JDK 17
2. Install Maven 3.9+
3. Install Chrome and/or Edge browser

Quick check:
```bash
java -version
mvn -version
```

## Environment Setup
Framework reads environment config via OWNER from:
- `src/test/resources/environmentConfig/testEnvironment.properties`

Required keys:
- `App.url`
- `App.email`
- `App.password`

Default TestNG suite in this repo:
- `src/test/resources/runTest/testng-local.xml`

## Run Tests
Run from project root (`jobs` folder).

### 1) Default run (Chrome desktop)
```bash
mvn clean test -DfileName=testng-local.xml
```

### 2) Run by browser
Supported values:
- `CHROME`
- `H_CHROME`
- `EDGE`
- `H_EDGE`

Examples:
```bash
mvn clean test -DfileName=testng-local.xml -DBROWSER=CHROME
mvn clean test -DfileName=testng-local.xml -DBROWSER=EDGE
mvn clean test -DfileName=testng-local.xml -DBROWSER=H_CHROME
mvn clean test -DfileName=testng-local.xml -DBROWSER=H_EDGE
```

### 3) Run with mobile emulation
Supported mobile devices:
- `IPHONE_13`
- `GALAXY_S9`

Examples:
```bash
mvn clean test -DfileName=testng-local.xml -DBROWSER=CHROME -DMOBILE_DEVICE=IPHONE_13
mvn clean test -DfileName=testng-local.xml -DBROWSER=EDGE -DMOBILE_DEVICE=GALAXY_S9
```

### 4) Use environment variables (optional)
```bash
export BROWSER=CHROME
export MOBILE_DEVICE=IPHONE_13
mvn clean test -DfileName=testng-local.xml
```

## Generate Allure Report
After test run:
```bash
mvn allure:report
```

Open generated report at:
- `target/allure-report/index.html`

Or run live server:
```bash
mvn allure:serve
```

## Notes
- Browser is resolved from `-DBROWSER`, then `BROWSER` env var, then defaults to `CHROME`.
- Mobile emulation is resolved from `-DMOBILE_DEVICE` or `MOBILE_DEVICE` env var.
- If `MOBILE_DEVICE` is unsupported, framework logs supported values and runs without emulation.
