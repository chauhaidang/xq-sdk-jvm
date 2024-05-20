plugins {
    id("my-application")
}

dependencies {
    implementation(project(":data-model"))
    implementation(project(":business-logic"))
    implementation(project(":common"))
}

application {
   mainClass.set("com.xq.Main")
}