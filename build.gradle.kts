plugins {
    java
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    pmd
    id("com.github.spotbugs") version "6.1.7"
    jacoco
    checkstyle
}

group = "com.xmljim.retirement"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    // Database
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    // OpenAPI / Swagger
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.4")

    // Development
    developmentOnly("org.springframework.boot:spring-boot-devtools")

    // Testing - JUnit 5
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // SpotBugs annotations
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.3")
}

// JUnit 5
tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

// PMD
pmd {
    toolVersion = "7.12.0"
    isConsoleOutput = true
    ruleSetFiles = files("config/pmd/ruleset.xml")
    ruleSets = listOf()
}

tasks.withType<Pmd> {
    reports {
        xml.required = true
        html.required = true
    }
}

// SpotBugs
spotbugs {
    toolVersion = "4.9.3"
    excludeFilter = file("config/spotbugs/exclude.xml")
}

tasks.withType<com.github.spotbugs.snom.SpotBugsTask> {
    reports.create("html") {
        required = true
        outputLocation = file("${layout.buildDirectory.get()}/reports/spotbugs/${name}.html")
    }
    reports.create("xml") {
        required = true
        outputLocation = file("${layout.buildDirectory.get()}/reports/spotbugs/${name}.xml")
    }
}

// JaCoCo
jacoco {
    toolVersion = "0.8.13"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        html.required = true
    }
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.50".toBigDecimal()
            }
        }
    }
}

// Checkstyle - Sun/Oracle conventions
checkstyle {
    toolVersion = "10.23.1"
    config = resources.text.fromUri("https://raw.githubusercontent.com/checkstyle/checkstyle/checkstyle-10.23.1/src/main/resources/sun_checks.xml")
    isIgnoreFailures = false
}

tasks.withType<Checkstyle> {
    reports {
        xml.required = true
        html.required = true
    }
}

// Quality gate task
tasks.register("qualityGate") {
    description = "Runs all quality checks"
    group = "verification"
    dependsOn(
        tasks.test,
        tasks.pmdMain,
        tasks.pmdTest,
        tasks.spotbugsMain,
        tasks.spotbugsTest,
        tasks.checkstyleMain,
        tasks.checkstyleTest,
        tasks.jacocoTestReport,
        tasks.jacocoTestCoverageVerification
    )
}

// Make check depend on all quality tools
tasks.check {
    dependsOn(tasks.named("qualityGate"))
}
