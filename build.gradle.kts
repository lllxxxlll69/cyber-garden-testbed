plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.spring") version "2.0.21"
    id("org.springframework.boot") version "3.3.5"
}

repositories { mavenCentral() }
kotlin { jvmToolchain(17) }

configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("garden.training:demo-codec")).using(project(":demo-codec"))
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.3.5"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    runtimeOnly("com.h2database:h2")
    // Synthetic advisory fixture; resolved to the local subproject, not Maven Central.
    implementation("garden.training:demo-codec:1.1.0")
}
