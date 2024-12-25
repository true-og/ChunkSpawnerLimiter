import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml)
}
version = "4.3.10"
description = "Limit entities in chunks."

val customMavenLocal = System.getProperty("SELF_MAVEN_LOCAL_REPO")
if (customMavenLocal != null) {
    val mavenLocalDir = file(customMavenLocal)
    if (mavenLocalDir.isDirectory) {
        println("Using SELF_MAVEN_LOCAL_REPO at: $customMavenLocal")
        repositories {
            maven {
                url = uri("file://${mavenLocalDir.absolutePath}")
            }
        }
    } else {
        logger.error("TrueOG Bootstrap not found, defaulting to ~/.m2 for mavenLocal()")
    }
} else {
    logger.error("TrueOG Bootstrap not found, defaulting to ~/.m2 to mavenLocal()")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    mavenLocal()
}

dependencies {
    compileOnly(libs.spigot.api)

    implementation(libs.bstats)
    implementation(libs.acf)
    implementation(libs.annotations)
}

bukkit {
    name = "ChunkSpawnerLimiter"
    main = "com.cyprias.chunkspawnerlimiter.ChunkSpawnerLimiter"
    version = project.version.toString()
    apiVersion = "1.14"
    authors = listOf("Cyprias", "sarhatabaot")
    website = "https://github.com/Cyprias/ChunkSpawnerLimiter"
    description = "Limit entities in chunks."
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    prefix = "CSL"
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        minimize()

        exclude("META-INF/**")

        archiveFileName.set("chunkspawnerlimiter-${project.version}.jar")
        archiveClassifier.set("shadow")

        relocate("org.bstats", "com.cyprias.chunkspawnerlimiter.libs.bstats")
        relocate("de.tr7zw.changeme.nbtapi", "com.cyprias.chunkspawnerlimiter.libs.nbt")
        relocate("co.aikar.commands", "com.cyprias.chunkspawnerlimiter.libs.acf")
        relocate("co.aikar.locales", "com.cyprias.chunkspawnerlimiter.libs.locales")
    }

    compileJava {
        options.compilerArgs.add("-parameters")
        options.isFork = true
        options.encoding = "UTF-8"
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
