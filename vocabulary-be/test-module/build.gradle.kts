plugins {
    id("build-jvm")
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.3"
//    id("io.spring.dependency-management") version "1.1.7"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation("org.springframework.boot:spring-boot-starter-web:4.0.3")
    implementation("net.logstash.logback:logstash-logback-encoder:9.0")
}