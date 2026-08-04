object BuildConfig {
    const val JAVA_VERSION: Int = 21

    const val MINECRAFT_VERSION_RANGE: String = ">=1.21.11"
    val MINECRAFT_VERSION_MIN: String = MINECRAFT_VERSION_RANGE.split(" ")[0].replace(Regex("^[><=!\\[\\]()]+"), "")
    const val MINECRAFT_VERSION: String = "1.21.11"
    const val FABRIC_LOADER_VERSION: String = "0.19.2"
    const val FABRIC_API_VERSION: String = "0.141.5+1.21.11"

    var MOD_VERSION: String = "0.1.3"
}

plugins {
    id("java-library")
    id("net.fabricmc.fabric-loom-remap") version("1.17.+")
    id("maven-publish")
}

base {
    archivesName = "displayname"
}

group = "kr.pyke"
version = createVersionString()

repositories {
    mavenCentral()
    maven("https://maven.parchmentmc.org")
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-1.21.11:2025.12.20@zip")
    })
    modImplementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${BuildConfig.FABRIC_API_VERSION}")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks {
    processResources {
        val propertiesMap = mapOf(
            "version" to version,
            "minecraft_version" to BuildConfig.MINECRAFT_VERSION_MIN
                .replace("(?<=\\D)-".toRegex(), "."), // fabric snapshot test
            "fabric_loader_version" to BuildConfig.FABRIC_LOADER_VERSION,
            "fabric_api_version" to BuildConfig.FABRIC_API_VERSION
        )
        inputs.properties(propertiesMap)
        filesMatching(listOf("fabric.mod.json")) {
            expand(propertiesMap)
        }
    }

    remapJar {
        //from("LICENSE")
        destinationDirectory.set(layout.buildDirectory.dir("mods"))
    }
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(BuildConfig.JAVA_VERSION)

    withSourcesJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release.set(BuildConfig.JAVA_VERSION)
}

tasks.test {
    useJUnitPlatform()
}

fun createVersionString(): String {
    val builder = StringBuilder()

    val isReleaseBuild = project.hasProperty("build.release")

    if (isReleaseBuild) {
        builder.append(BuildConfig.MOD_VERSION)
    } else {
        builder.append(BuildConfig.MOD_VERSION.substringBefore('-'))
        builder.append("-snapshot")
    }

    builder.append("+mc").append(BuildConfig.MINECRAFT_VERSION)

    return builder.toString()
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}