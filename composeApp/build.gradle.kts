import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.moko.resources)
}

kotlin {
    jvm("desktop")

    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            api(libs.moko.resources)
            api(libs.moko.resources.compose)
        }
        multiplatformResources {
            resourcesPackage.set("id.neotica.modernadb.res")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}


compose.desktop {
    application {
        mainClass = "id.neotica.modernadb.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Rpm)
            packageName = "ModernADB"
            packageVersion = "1.0.0"

            macOS {
                val generatedIcon = layout.buildDirectory.file("generated/moko-resources/desktopMain/res/files/ModernADB.icns")
                iconFile.set(generatedIcon)
            }
        }
    }
}

// This creates a new custom task named "packageAll"
tasks.register("packageAll") {
    // Optional: This puts your task in a "distribution" folder in the Gradle tool window
    group = "distribution"
    description = "Builds all native distribution packages (MSI, DMG, DEB, RPM)."

    // This is the key part: it tells Gradle to run these tasks first
    val packageTasks = listOf("packageMsi", "packageDmg", "packageDeb", "packageRpm")
    packageTasks.forEach { taskName ->
        tasks.findByName(taskName)?.let {
            dependsOn(it)
        }
    }
}