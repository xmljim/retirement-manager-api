plugins {
    java
    id("org.springframework.boot") version "4.0.2"
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

// Force ASM 9.9.1 for Java 25 compatibility (class file version 69)
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.ow2.asm") {
            useVersion("9.9.1")
            because("Java 25 requires ASM 9.9+")
        }
    }
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
    testImplementation("org.springframework.boot:spring-boot-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter:1.20.4")
    testImplementation("org.testcontainers:postgresql:1.20.4")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // SpotBugs annotations
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.9.8")

    // Force latest byte-buddy and ASM for Java 25 compatibility
    testImplementation("net.bytebuddy:byte-buddy:1.17.1")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.17.1")
    testImplementation("org.ow2.asm:asm:9.9.1")
}

// JUnit 5
tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    // Required for Mockito with Java 21+
    jvmArgs(
        "--add-opens", "java.base/java.lang=ALL-UNNAMED",
        "--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED",
        "--add-opens", "java.base/java.util=ALL-UNNAMED",
        "-XX:+EnableDynamicAgentLoading"
    )
}

// PMD
pmd {
    toolVersion = "7.16.0"
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
    toolVersion = "4.9.8"
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

// Checkstyle - Sun/Oracle conventions with 120 char line length
checkstyle {
    toolVersion = "10.23.1"
    configFile = file("config/checkstyle/checkstyle.xml")
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
