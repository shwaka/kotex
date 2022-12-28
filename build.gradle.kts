plugins {
    kotlin("multiplatform") version "1.5.32"
    id("com.adarshr.test-logger") version "3.0.1-SNAPSHOT"
    `maven-publish`
}

group = "me.shun"
version = "0.1"

repositories {
    mavenCentral()
}

kotlin {
    explicitApiWarning()
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }
    js(LEGACY) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
            }
        }
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                // kotest
                val version = "4.3.2"
                implementation("io.kotest:kotest-runner-junit5:$version")
                implementation("io.kotest:kotest-assertions-core:$version")
                implementation("io.kotest:kotest-property:$version")
                implementation("io.kotest:kotest-assertions-compiler:$version")
                // Explicit dependency on kotlin-reflect and kotlin-script-runtime is necessary
                // to avoid "Runtime JAR files in the classpath should have the same version"
                // in kotest-assertions-compiler.
                implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.32")
                implementation("org.jetbrains.kotlin:kotlin-script-runtime:1.5.32")
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("skipped", "passed", "failed") // "started" は消した
    }
    System.getProperty("kotest.tags")?.let {
        // null を set するとなんかエラーが起きるので、 ?.let を使った
        systemProperties["kotest.tags"] = it
    }
    System.getProperty("kococo.debug")?.let {
        systemProperties["kococo.debug"] = it
    }
}

testlogger {
    theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
    showCauses = true
    showStandardStreams = true
    showFullStackTraces = true
    if (System.getProperty("noFilter") == null) {
        filterFullStackTraces = "io\\.kotest.*"
    }
}

publishing {
    repositories {
        maven {
            url = uri("../maven/repository")
            name = "MyMaven"
        }
        // maven {
        //     url = uri("../repository")
        //     name = "Temporary"
        // }
    }
}

// aliases
tasks.register("kc") { dependsOn("ktlintCheck") }
tasks.register("kf") { dependsOn("ktlintFormat") }
