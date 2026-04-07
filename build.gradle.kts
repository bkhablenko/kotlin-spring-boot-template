import org.gradle.api.tasks.testing.logging.TestLogEvent.FAILED
import org.gradle.api.tasks.testing.logging.TestLogEvent.SKIPPED
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html#0.3
val mockitoAgent: Configuration = configurations.create("mockitoAgent")

plugins {
    id("com.github.ben-manes.versions") version "0.53.0"

    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
    kotlin("jvm") version "2.3.20"
    kotlin("plugin.spring") version "2.3.20"
    kotlin("plugin.jpa") version "2.3.20"

    // Must declare after Spring Boot plugin.
    // See: https://github.com/springdoc/springdoc-openapi-gradle-plugin/issues/121
    id("org.springdoc.openapi-gradle-plugin") version "1.9.0"
}

group = "com.github.bkhablenko"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    mockitoAgent("org.mockito:mockito-core") { isTransitive = false }

    // Spring Boot
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-opentelemetry")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin") // Hibernate still needs Jackson 2
    implementation("com.fasterxml.uuid:java-uuid-generator:5.2.0")
    implementation("io.github.oshai:kotlin-logging:8.0.01")
    implementation("io.hypersistence:hypersistence-utils-hibernate-71:3.15.2")
    implementation("org.flywaydb:flyway-database-postgresql")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.2")
    implementation("tools.jackson.module:jackson-module-kotlin")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-actuator-test")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.springframework.boot:spring-boot-starter-security-test")
    testImplementation("org.springframework.boot:spring-boot-starter-validation-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.mockito.kotlin:mockito-kotlin:6.3.0")

    // Testcontainers
    testImplementation("org.testcontainers:testcontainers-junit-jupiter")
    testImplementation("org.testcontainers:testcontainers-postgresql")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
        jvmTarget = JvmTarget.JVM_21
    }
}

allOpen {
    annotation("jakarta.persistence.Embeddable")
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
}

openApi {
    apiDocsUrl = "http://localhost:9080/actuator/openapi"
    customBootRun {
        jvmArgs = listOf("-Duser.timezone=UTC")

        // https://github.com/springdoc/springdoc-openapi-gradle-plugin/issues/150
        systemProperties = mapOf("spring.docker.compose.file" to projectDir.resolve("compose.yaml"))
    }
}

tasks {
    bootJar {
        archiveFileName = "${project.name}.jar"
    }
    dependencyUpdates {
        gradleReleaseChannel = "current"
        revision = "release"
    }
    test {
        jvmArgs = listOf(
            "-javaagent:${mockitoAgent.asPath}",
            "-Dspring.profiles.active=test",
            "-Duser.timezone=UTC",
            "-Xshare:off",
        )
        useJUnitPlatform()
        testLogging {
            events(SKIPPED, FAILED)
        }
    }
    wrapper {
        gradleVersion = "9.4.1"
    }
}
