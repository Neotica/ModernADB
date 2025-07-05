import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
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
            packageName = "id.neotica.modernadb"
            packageVersion = "1.0.0"
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