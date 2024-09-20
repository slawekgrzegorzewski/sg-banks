import pl.sg.build.CodeArtifactAccess
import pl.sg.release.VersionUtil

group = "pl.sg"
version = "0.0.1-SNAPSHOT"

file("../dev-ops/docker/currency.properties")
    .copyTo(
        file("${System.getProperty("java.home")}\\lib\\currency.properties"),
        overwrite = true)

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

plugins {
    java
    id("org.springframework.boot") version "3.2.1"
    id("io.spring.dependency-management") version "1.1.4"
    id("maven-publish")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://sg-repository-215372400964.d.codeartifact.eu-central-1.amazonaws.com/maven/sg-repository/")
        credentials {
            username = "aws"
            password = CodeArtifactAccess.getToken()
        }
    }
}

dependencies {
    implementation(project(":sg-go-cardless-integration"))

    implementation("net.logstash.logback:logstash-logback-encoder:7.2")
    implementation("org.slf4j:jul-to-slf4j:2.0.16")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("pl.sg:sg-utils:1.0.0")

    compileOnly("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql:42.5.1")

    annotationProcessor("org.projectlombok:lombok")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("org.postgresql:postgresql:42.5.1")
}

publishing {
    publications {
        create<MavenPublication>("releaseCurrent") {
            groupId = "pl.sg"
            artifactId = "sg-banks-app"
            version = VersionUtil.getCurrentVersion(project.rootDir).toString()
            from(components["java"])
        }
    }

    repositories {
        maven {
            url =
                uri("https://sg-repository-215372400964.d.codeartifact.eu-central-1.amazonaws.com/maven/sg-repository/")
            credentials {
                username = "aws"
                password = CodeArtifactAccess.getToken()
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.create("getCurrentSemver", Task::class) {
    doLast {
        file(project.rootDir.resolve("current.semver")).appendText(VersionUtil.getCurrentVersion(project.rootDir).toString())
    }
}

tasks.create("getNextSemver", Task::class) {
    doLast {
        file(project.rootDir.resolve("next.semver")).appendText(VersionUtil.getNextVersion(project.rootDir, System.getenv("LEVEL")).toString())
    }
}