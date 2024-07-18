plugins {
    id("java-platform")
}

javaPlatform.allowDependencies()

dependencies {
    api(platform("com.fasterxml.jackson:jackson-bom:2.14.2"))
    //add junit5 dependency
    api("org.junit.jupiter:junit-jupiter-api:5.10.0")
    api("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

group = "com.xq"

dependencies.constraints {
    api("org.apache.commons:commons-lang3:3.12.0")
    api("org.slf4j:slf4j-api:1.7.36")
    api("org.slf4j:slf4j-simple:1.7.36")
    api("com.linecorp.armeria:armeria:1.26.0")
}