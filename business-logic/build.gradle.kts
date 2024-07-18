plugins {
    id("xq-base")
}

group = "com.xq"
version = "0.0.1"

dependencies {
    //Dependencies will be resolved automatically by what is already defined in platform module
    implementation(platform("com.xq:platform"))
    implementation(project(":data-model"))
    implementation("org.apache.commons:commons-lang3")
    implementation("org.slf4j:slf4j-api")
    functionalTestImplementation(platform("com.xq:platform"))
    functionalTestImplementation(project(":data-model"))
}
tasks.test {
    useJUnitPlatform {
        excludeTags("slow")
    }
    maxParallelForks = 4
    maxHeapSize = "1g"
}

tasks.register<Test>("testSlow") {
    group = "xq"
    testClassesDirs = sourceSets.test.get().output.classesDirs
    classpath = sourceSets.test.get().runtimeClasspath
    useJUnitPlatform {
        includeTags("slow")
    }
}

tasks.check {
    dependsOn("CustomTest")
}