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

val docker by configurations.creating

configurations {
    docker
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

    docker("pl.sg:sg-banks-app:1.0.2")
}

val dockerPackage = tasks.register<Zip>("dockerPackage") {
    archiveFileName.set("docker.zip")
    from(project.rootDir.resolve("dev-ops").resolve("docker")) {
        include("currency.properties")
        include("Dockerfile")
        include("Dockerfile-debug")
    }
    from(project.rootDir.resolve("sg-banks-app").resolve("build").resolve("libs")) {
        include("sg-banks-app-0.0.1-SNAPSHOT.jar").rename { "sg-banks-app.jar" }
    }
    dependsOn(tasks.build)
}

publishing {
    publications {
        create<MavenPublication>("releaseCurrent") {
            groupId = "pl.sg"
            artifactId = "sg-banks-app"
            version = VersionUtil.getCurrentVersion(project.rootDir).toString()
            artifact(dockerPackage)
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

val getCurrentSemver by tasks.creating(Task::class) {
    doLast {
        file(project.rootDir.resolve("current.semver")).appendText(
            VersionUtil.getCurrentVersion(project.rootDir).toString()
        )
    }
}

val getNextSemver by tasks.creating(Task::class) {
    doLast {
        file(project.rootDir.resolve("next.semver")).appendText(
            VersionUtil.getNextVersion(
                project.rootDir,
                System.getenv("LEVEL")
            ).toString()
        )
    }
}

val prepareDockerDistribution by tasks.creating {
    doLast {
        val files = docker.resolve()
        if (files.size != 1) throw RuntimeException("Too much files")
        files.forEach {
            file(project.rootDir.resolve("docker_zip.txt")).appendText(
                it.absolutePath
            )
        }
    }
}