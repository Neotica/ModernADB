// ========= File: adb/android/AdbInput.kt =========

package id.neotica.modernadb.adb.android

import java.io.File
import java.util.concurrent.TimeUnit

object AdbInput {
    var selectedDevice: String? = null

    private val adbExecutablePath: String by lazy {
        val sdkDirs = listOfNotNull(
            System.getenv("ANDROID_SDK_ROOT"),
            System.getenv("ANDROID_HOME"),
            "${System.getProperty("user.home")}/Library/Android/sdk",
            "/usr/local/share/android-sdk",
            "/opt/android-sdk"
        )

        val found = sdkDirs
            .map { File(it, "platform-tools/adb") }
            .firstOrNull { it.exists() }

        found?.absolutePath ?: error("ADB not found in common locations. Please install Android SDK.")
    }

    private fun exec(cmd: String, waitAfter: Long = 100): Process {
        val fullCommand = cmd.replaceFirst("adb", adbExecutablePath)
        println("DEBUG: Executing command: '$fullCommand'")
        val test = "adb devices"
        val formattedSelectedAdb = fullCommand.replaceFirst("adb", "adb -s $selectedDevice")

        println("modernadb: ${test.replaceFirst("adb", "adb -s $selectedDevice")}")

        val processBuilder = if (System.getProperty("os.name").startsWith("Windows")) {
            ProcessBuilder("cmd.exe", "/c", formattedSelectedAdb)
        } else {
            ProcessBuilder("sh", "-c", formattedSelectedAdb)
        }

        val environment = processBuilder.environment()
        val userHome = System.getProperty("user.home")
        environment["HOME"] = userHome
        environment["ANDROID_SDK_HOME"] = "$userHome/.android"
        println("DEBUG: Setting HOME for process to: $userHome")

        val process = processBuilder.redirectErrorStream(true).start()

        // Wait for the process to finish and check its exit code
        val finished = process.waitFor(5, TimeUnit.SECONDS)
        if (finished) {
            println("DEBUG: Process finished with exit code: ${process.exitValue()}")
        } else {
            println("DEBUG: Process timed out. Forcibly destroying.")
            process.destroyForcibly()
        }

        Thread.sleep(waitAfter)
        return process
    }

    fun getDevices(): String {
        return try {
            val process = exec("adb devices")
            val reader = process.inputStream.bufferedReader().readText()
            val errors = process.errorStream.bufferedReader().readText()
            val combined = "$reader$errors"
            println(combined)
            combined
        } catch (e: Exception) {
            val errorMsg = "ERROR reading deviceList stream: ${e.message}"
            println(errorMsg)
            errorMsg
        }
    }

    fun getDeviceList(): List<String> {
        return try {
            val process = exec("adb devices")
            val reader = process.inputStream.bufferedReader().readText()
            val result = reader.lines().drop(1).mapNotNull {
                val serial = it.split('\t').firstOrNull()
                serial?.takeIf { serial.isNotBlank() }
            }

            println("result: $result")
            result
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun powerButton() = exec("adb shell input keyevent 26")
    fun longPressPowerButton() = exec("adb shell input keyevent --longpress 26")

    //power
    fun shutdownLegacy() = exec("adb shell shutdown")
    fun shutdown() = exec("adb reboot -p")
    fun reboot() = exec("adb reboot")
    fun rebootRecovery() = exec("adb reboot recovery")
    fun rebootBootloader() = exec("adb reboot bootloader")


    fun isAwake(): Boolean {
        return try {
            val output = exec("adb shell dumpsys power | grep \"mWakefulness=\"").inputStream.bufferedReader().readText()
            print(output)
            output.contains("Awake")
        } catch (e: Exception) {
            println("ERROR reading isAwake stream: ${e.message}")
            false
        }
    }

    fun unlock(password: String) {
        val maxAttempts = 3
        var currentAttempt = 0

        while (currentAttempt < maxAttempts) {
            try {
                val output = exec("adb shell dumpsys power | grep \"mWakefulness=\"").inputStream.bufferedReader().readText()
                print("output: $output")
                if (output.contains("Awake")) {
                    println("Screen is ON.")
                    exec("adb shell input swipe 500 1000 500 500 200")
                    exec("adb shell input text \"$password\"")
                    break // Exit the loop
                }
            } catch (e: Exception) {
                println("ERROR during unlock check: ${e.message}")
            }

            println("Screen is OFF. Sending wakeup command (Attempt ${currentAttempt + 1})...")
            exec("adb shell input keyevent KEYCODE_WAKEUP")
            Thread.sleep(500)
            currentAttempt++
        }
    }

    fun sendText(message: String) {
        val formatted = formatMessage(message)
        exec("adb shell input text \"$formatted\"")
    }

    fun sendEnter() = exec("adb shell input keyevent KEYCODE_ENTER")
    fun sendKey(keyCode: Int) = exec("adb shell input keyevent $keyCode")
    fun logCat() = exec("adb shell logcat")

    fun homeButton() = exec("adb shell am start -W -c android.intent.category.HOME -a android.intent.action.MAIN")
    fun backButton() = exec("adb shell input keyevent KEYCODE_BACK")
    fun recentButton() = exec("adb shell am start -n com.android.systemui/com.android.systemui.recents.RecentsActivity")
    fun switchApp() = exec("adb shell input keyevent KEYCODE_APP_SWITCH")

    fun nextButton() = exec("adb shell input keyevent 22")
    fun prevButton() = exec("adb shell input keyevent 21")
    fun upButton() = exec("adb shell input keyevent 19")
    fun downButton() = exec("adb shell input keyevent 20")

    fun backspaceButton() = exec("adb shell input keyevent 67")
    fun tabButton() = exec("adb shell input keyevent 61")
    fun selectAll() = exec("adb shell input keycombination 113 29")
    fun shiftTab() = exec("adb shell input keycombination 59 61")

    fun touchInput(x: Int, y: Int) = exec("adb shell input tap $x $y")

    fun forceClose() {
        switchApp()
        holdInputTime(500 , 1000, endY = 100, time = 100)
    }
    fun swipeUp() = holdInputTime(500, 2000, 500, 500, 100)
    fun swipeDown() = holdInputTime(500, 500, 500, 2000, 100)
    fun holdInputTime(
        startX: Int, startY: Int, endX: Int? = startX, endY: Int? = startY, time: Int? = 500
    ) = exec("adb shell input swipe $startX $startY $endX $endY $time")

    fun activityManager() = exec("adb shell am start -a android.intent.action.VIEW")

    private fun formatMessage(input: String): String {
        return input.replace(" ", "%s")
            .replace("&", "\\&")
            .replace("(", "\\(")
            .replace(")", "\\)")
            .replace("<", "\\<")
            .replace(">", "\\>")
            .replace("\"", "\\\"")
            .replace(";", "\\;")
    }
}