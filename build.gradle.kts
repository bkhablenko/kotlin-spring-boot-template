import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.api.tasks.wrapper.Wrapper.DistributionType

plugins {
    id("org.springframework.boot") version "3.1.5"
    id("io.spring.dependency-management") version "1.1.3"
    kotlin("jvm") version "1.9.10"
    kotlin("plugin.spring") version "1.9.10"
    kotlin("plugin.jpa") version "1.9.10"
}

group = "com.github.bkhablenko"
version = "0.1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.uuid:java-uuid-generator:4.3.0")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")
    implementation("org.codehaus.janino:janino")
    implementation("org.flywaydb:flyway-core")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.1.0")
}

tasks {
    compileKotlin {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
            jvmTarget = "17"
        }
    }
    test {
        useJUnitPlatform()
        testLogging {
            events = setOf(TestLogEvent.SKIPPED, TestLogEvent.FAILED)
        }
    }
    wrapper {
        distributionType = DistributionType.ALL
        gradleVersion = "8.4"
    }
}
