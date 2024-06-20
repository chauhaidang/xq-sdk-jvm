plugins {
    id("xq-base")
}

group = "com.xq"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":data-model"))
    implementation("org.apache.commons:commons-lang3:3.12.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

}

tasks.test {
    useJUnitPlatform()
}