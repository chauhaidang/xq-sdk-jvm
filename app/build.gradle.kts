plugins {
    id("application")
}

dependencies {
    implementation(platform("com.xq:platform"))
    implementation(project(":data-model"))
    implementation(project(":business-logic"))
    implementation(project(":common"))
    runtimeOnly("org.slf4j:slf4j-simple");
}

application {
   mainClass.set("com.xq.Main")
}