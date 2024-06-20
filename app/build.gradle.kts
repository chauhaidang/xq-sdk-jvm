plugins {
    id("application")
}

dependencies {
    implementation(project(":data-model"))
    implementation(project(":business-logic"))
    implementation(project(":common"))
    runtimeOnly("org.slf4j:slf4j-simple:1.7.36");
}

application {
   mainClass.set("com.xq.Main")
}