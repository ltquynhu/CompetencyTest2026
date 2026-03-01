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
Run from project root (folder has pom.xml file).

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

## CI/CD Integration (Step-by-Step)

### Part A: Integrate this test into CI/CD

### Jenkins-first setup

#### Step 0: Prepare Jenkins plugins and tools
Install/enable:
- Pipeline
- Git
- Credentials Binding
- JUnit
- HTML Publisher (optional)

Configure Global Tools:
- JDK 17
- Maven 3.9+

#### Step 1: Define pipeline stages
Create 3 stages:
1. **PR Validation** (fast)
2. **Main Branch Validation** (full)
3. **Nightly Regression** (extended)

#### Step 2: Configure pipeline triggers
- Use a **Multibranch Pipeline** job to build branches and PRs automatically.
- Run **PR Validation** when a PR branch is indexed/built.
- Run **Main Branch Validation** when branch is `main`.
- Run **Nightly Regression** using Jenkins cron trigger (for example: `H 2 * * *`).

#### Step 3: Add CI secrets
Create Jenkins Credentials (example IDs):
- `linkedin-app-url` (Secret text)
- `linkedin-app-email` (Secret text)
- `linkedin-app-password` (Secret text)

These map to runtime values:
- `APP_URL`
- `APP_EMAIL`
- `APP_PASSWORD`

#### Step 4: Generate runtime environment config
Before running tests, generate:

```bash
cat > src/test/resources/environmentConfig/testEnvironment.properties <<EOF
App.url=${APP_URL}
App.email=${APP_EMAIL}
App.password=${APP_PASSWORD}
EOF
```

In Jenkins, generate this file inside a `withCredentials` block.

#### Step 5: Run tests in matrix mode
Use browser/device matrix by stage:

- **PR Validation**
	- `CHROME` (or `H_CHROME`)
	- Command:
	```bash
	mvn clean test -DfileName=testng-local.xml -DBROWSER=CHROME
	```

- **Main Branch Validation**
	- `CHROME`, `EDGE`
	- Commands:
	```bash
	mvn clean test -DfileName=testng-local.xml -DBROWSER=CHROME
	mvn clean test -DfileName=testng-local.xml -DBROWSER=EDGE
	```

- **Nightly Regression**
	- `CHROME`, `EDGE`, plus mobile emulation:
	```bash
	mvn clean test -DfileName=testng-local.xml -DBROWSER=CHROME -DMOBILE_DEVICE=IPHONE_13
	mvn clean test -DfileName=testng-local.xml -DBROWSER=EDGE -DMOBILE_DEVICE=GALAXY_S9
	```

In Jenkins, this is best done with a Declarative `matrix` stage.

#### Step 6: Generate and publish reports
After test execution:

```bash
mvn allure:report
```

Publish these artifacts in CI:
- `target/surefire-reports`
- `target/allure-results`
- `target/allure-report`

For Jenkins UI visibility:
- Publish JUnit XML from `target/surefire-reports/*.xml`
- Archive `target/allure-results/**` and `target/allure-report/**`

#### Step 7: Add quality gates
- Fail PR build when critical tests fail.
- Keep flaky/non-critical tests out of PR gate (run nightly).
- Add execution timeout and retry policy as needed.

### Example Jenkinsfile (Declarative)

```groovy
pipeline {
	agent any

	tools {
		jdk 'jdk17'
		maven 'maven-3.9'
	}

	options {
		timestamps()
		ansiColor('xterm')
		disableConcurrentBuilds()
		buildDiscarder(logRotator(numToKeepStr: '20'))
		timeout(time: 60, unit: 'MINUTES')
	}

	triggers {
		// Nightly run at a hashed time around 2 AM
		cron('H 2 * * *')
	}

	environment {
		SUITE_FILE = 'testng-local.xml'
	}

	stages {
		stage('Checkout') {
			steps {
				checkout scm
			}
		}

		stage('Prepare env config') {
			steps {
				withCredentials([
					string(credentialsId: 'linkedin-app-url', variable: 'APP_URL'),
					string(credentialsId: 'linkedin-app-email', variable: 'APP_EMAIL'),
					string(credentialsId: 'linkedin-app-password', variable: 'APP_PASSWORD')
				]) {
					sh '''
						cat > src/test/resources/environmentConfig/testEnvironment.properties <<EOF
						App.url=${APP_URL}
						App.email=${APP_EMAIL}
						App.password=${APP_PASSWORD}
						EOF
					'''
				}
			}
		}

		stage('PR Validation') {
			when {
				anyOf {
					changeRequest()
					expression { env.BRANCH_NAME != 'main' }
				}
			}
			steps {
				sh 'mvn -B clean test -DfileName=${SUITE_FILE} -DBROWSER=CHROME'
			}
		}

		stage('Main / Nightly Matrix') {
			when {
				branch 'main'
			}
			matrix {
				axes {
					axis {
						name 'BROWSER'
						values 'CHROME', 'EDGE'
					}
					axis {
						name 'MOBILE_DEVICE'
						values '', 'IPHONE_13', 'GALAXY_S9'
					}
				}
				stages {
					stage('Run') {
						steps {
							script {
								if (MOBILE_DEVICE?.trim()) {
									sh "mvn -B clean test -DfileName=${SUITE_FILE} -DBROWSER=${BROWSER} -DMOBILE_DEVICE=${MOBILE_DEVICE}"
								} else {
									sh "mvn -B clean test -DfileName=${SUITE_FILE} -DBROWSER=${BROWSER}"
								}
							}
						}
					}
				}
			}
		}

		stage('Generate Allure Report') {
			steps {
				sh 'mvn -B allure:report'
			}
		}
	}

	post {
		always {
			junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
			archiveArtifacts artifacts: 'target/allure-results/**, target/allure-report/**', allowEmptyArchive: true
		}
	}
}
```

### Jenkins maintenance checklist
- Keep credentials rotated and never hardcode account data.
- Re-run failed builds once to detect flaky behavior.
- Track average duration per stage and set alerts on regressions.
- Clean old artifacts/workspace regularly to avoid disk pressure.

---

### Part B: Maintain this automation over time (Step-by-Step)

#### Step 1: Weekly failure triage
- Review failed tests from CI.
- Classify root cause: product defect, locator issue, data issue, or flaky infra.

#### Step 2: Weekly flaky test control
- Track flaky tests separately.
- Quarantine unstable tests from PR gate until fixed.

#### Step 3: Bi-weekly locator maintenance
- Recheck locators on frequently changing pages.
- Replace brittle XPath with stable attributes where possible.

#### Step 4: Monthly dependency updates
- Update `selenium`, `testng`, and `allure` in a controlled branch.
- Run full regression before merging.

#### Step 5: Quarterly framework cleanup
- Refactor shared framework code (`BaseTest`, `DriverFactory`, session management).
- Remove dead code and duplicated utility logic.

#### Step 6: Keep suite strategy healthy
- Keep PR suite small and fast.
- Keep full cross-browser + mobile coverage in scheduled runs.
- Rebalance test distribution as project scope grows.

#### Step 7: Keep observability strong
- Ensure screenshot/log attachments remain available for failures.
- Keep Allure trends/history to monitor stability over time.
