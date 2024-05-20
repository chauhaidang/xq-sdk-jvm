plugins {
    id("java")
    id("jacoco")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
