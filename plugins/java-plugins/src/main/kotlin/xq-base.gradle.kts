import com.xq.JarCount

plugins {
    id("java")
    id("jacoco")
}

group = "com.xq"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy("jacocoTestReport")
}

tasks.jacocoTestReport {
    dependsOn("test")
}

tasks.register<Zip>("xqBundle") {
    group = "XQ Base Plugins"
    description = "Bundle jar & files in class path"
    from(tasks.jar)
    from(configurations.runtimeClasspath)
    destinationDirectory.set(layout.buildDirectory.dir("dist"))
}

tasks.register<JarCount>("xqCountJars") {
    group = "XQ Base Plugins"
    description = "Count jars in class path"

    allJars.from(tasks.jar)
    allJars.from(configurations.runtimeClasspath)
    countFile.set(layout.buildDirectory.file("gen/count.txt"))
}

tasks.build {
    dependsOn(tasks.named("xqBundle"))
}

tasks.register("xqBuildAll") {
    group = "XQ Base Plugins"
    description = "Build all"
    dependsOn(tasks.named("build"))
    dependsOn(tasks.named("xqCountJars"))
}


