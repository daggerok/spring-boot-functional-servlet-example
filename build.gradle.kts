import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
  idea
  base
  java
  id("io.franzbecker.gradle-lombok") version "2.1"
  id("org.springframework.boot") version "2.2.0.BUILD-SNAPSHOT"
  id("io.spring.dependency-management") version "1.0.7.RELEASE"
}

group = "com.github.daggerok"
version = "1.0.0-SNAPSHOT"

val gradleVersion = "5.3"
val junitJupiterVersion = "5.4.0"
val javaVersion = JavaVersion.VERSION_11

extra["junit-jupiter.version"] = junitJupiterVersion

tasks.withType<Wrapper>().configureEach {
  gradleVersion = gradleVersion
  distributionType = Wrapper.DistributionType.BIN
}

java {
  sourceCompatibility = javaVersion
  targetCompatibility = javaVersion
}

repositories {
  mavenCentral()
  maven(url = "https://repo.spring.io/snapshot")
  maven(url = "https://repo.spring.io/milestone")
}

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-undertow")
  implementation("org.springframework.boot:spring-boot-starter-web") {
    exclude(module = "spring-boot-starter-tomcat")
  }
  implementation("org.springframework.boot:spring-boot-starter")

  annotationProcessor("org.projectlombok:lombok")
  testAnnotationProcessor("org.projectlombok:lombok")

  testImplementation("org.springframework.boot:spring-boot-starter-test")

  testImplementation("junit:junit")
  testImplementation(platform("org.junit:junit-bom:$junitJupiterVersion"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
  testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
  testRuntime("org.junit.platform:junit-platform-launcher")
}

tasks.withType<BootJar>().configureEach {
  launchScript()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    showExceptions = true
    showStandardStreams = true
    events(PASSED, SKIPPED, FAILED)
  }
}

defaultTasks("build")

tasks {
  getByName("clean") {
    doLast {
      delete(project.buildDir)
    }
  }
}

tasks.create<Zip>("sources") {
  dependsOn("clean")
  shouldRunAfter("clean", "assemble")
  description = "Archives sources in a zip file"
  group = "Archive"
  from("src") {
    into("src")
  }
  from(".gitignore")
  from(".java-version")
  from(".travis.yml")
  from("build.gradle.kts")
  from("pom.xml")
  from("README.md")
  from("settings.gradle.kts")
  archiveFileName.set("${project.buildDir}/sources-${project.version}.zip")
}
