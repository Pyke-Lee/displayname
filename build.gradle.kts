object BuildConfig {
    const val JAVA_VERSION: Int = 25

    const val MINECRAFT_VERSION_RANGE: String = ">=26.1" // range: ">=26.1 <27.1"
    val MINECRAFT_VERSION_MIN: String = MINECRAFT_VERSION_RANGE.split(" ")[0].replace(Regex("^[><=!\\[\\]()]+"), "")
    const val MINECRAFT_VERSION: String = "26.1.2"
    const val FABRIC_LOADER_VERSION: String = "0.19.2"
    const val FABRIC_API_VERSION: String = "0.147.0+26.1.2"

    var MOD_VERSION: String = "0.1.2"

    const val SIMPLE_VOICE_CHAT_MOD_VERSION: String = "fabric-2.6.16+26.1.2"
    const val PLASMO_VOICE_API_VERSION: String = "2.1.13"
    const val PLASMO_VOICE_MOD_VERSION: String = "fabric-26.1-2.1.13"
}

plugins {
    id("java-library")
    id("net.fabricmc.fabric-loom") version("1.16.+")
    id("maven-publish")
}

base {
    archivesName = "displayname"
}

group = "kr.pyke"
version = createVersionString()

repositories {
    mavenCentral()
    maven("https://repo.plasmoverse.com/releases")

    exclusiveContent {
        forRepository {
            maven("https://api.modrinth.com/maven")
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${BuildConfig.MINECRAFT_VERSION}")
    implementation("net.fabricmc:fabric-loader:${BuildConfig.FABRIC_LOADER_VERSION}")
    implementation("net.fabricmc.fabric-api:fabric-api:${BuildConfig.FABRIC_API_VERSION}")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Simple Voice Chat
    compileOnly("maven.modrinth:simple-voice-chat:${BuildConfig.SIMPLE_VOICE_CHAT_MOD_VERSION}")

    // Plasmo Voice
    compileOnly("su.plo.voice:protocol:${BuildConfig.PLASMO_VOICE_API_VERSION}")
    compileOnly("maven.modrinth:plasmo-voice:${BuildConfig.PLASMO_VOICE_MOD_VERSION}")
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

    jar {
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
    val buildId = System.getenv("GITHUB_RUN_NUMBER")

    if (isReleaseBuild) {
        builder.append(BuildConfig.MOD_VERSION)
    } else {
        builder.append(BuildConfig.MOD_VERSION.substringBefore('-'))
        builder.append("-snapshot")
    }

    builder.append("+mc").append(BuildConfig.MINECRAFT_VERSION)

    if (!isReleaseBuild) {
        if (buildId != null) {
            builder.append("-build.${buildId}")
        }
        else {
            builder.append("-local")
        }
    }

    return builder.toString()
}

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}