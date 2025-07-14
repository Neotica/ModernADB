package id.neotica.modernadb.data.adb.android

import java.io.File
import java.util.concurrent.TimeUnit

object AdbInput {

    // --- ADB Configuration ---
    var selectedDevice: String? = null

    private val adbExecutablePath: String by lazy {
        val sdkDirs = listOfNotNull(
            System.getenv("ANDROID_SDK_ROOT"),
            System.getenv("ANDROID_HOME"),
            "${System.getProperty("user.home")}/Library/Android/sdk",
            "/usr/local/share/android-sdk",
            "/opt/android-sdk",
        )

        val found = sdkDirs
            .map { File(it, "platform-tools/adb") }
            .firstOrNull { it.exists() }

        found?.absolutePath ?: error("ADB not found in common locations. Please install Android SDK.")
    }

    // --- Device Management ---
    fun getDevices(): String {
        return try {
            val process = exec("devices")
            process.inputStream.bufferedReader().readText()
        } catch (e: Exception) {
            val errorMsg = "ERROR reading deviceList stream: ${e.message}"
            println(errorMsg)
            errorMsg
        }
    }

    fun getDeviceList(): List<String> {
        return try {
            val rawOutput = getDevices()
            rawOutput.lines().drop(1).mapNotNull {
                val serial = it.split('\t').firstOrNull()
                serial?.takeIf { s -> s.isNotBlank() }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun connectWireless(port: String): String {
        val connect = exec("connect $port")
        return connect.inputStream.bufferedReader().readText()
    }

    fun pairWatch(port: String): String {
        val pair = exec("pair $port")
        return pair.inputStream.bufferedReader().readText()
    }

    // --- Power & Boot Commands ---
    fun powerButton() = exec("shell input keyevent 26")
    fun longPressPowerButton() = exec("shell input keyevent --longpress 26")
    fun shutdownLegacy() = exec("shell shutdown")
    fun shutdown() = exec("reboot -p")
    fun reboot() = exec("reboot")
    fun rebootRecovery() = exec("reboot recovery")
    fun rebootBootloader() = exec("reboot bootloader")

    fun isAwake(): Boolean {
        return try {
            val output = exec("shell dumpsys power | grep \"mWakefulness=\"")
                .inputStream.bufferedReader().readText()
            output.contains("Awake")
        } catch (e: Exception) {
            println("ERROR reading isAwake stream: ${e.message}")
            false
        }
    }

    fun unlock(password: String) {
        if (isAwake()) {
            println("Screen is ON.")
            exec("shell input swipe 500 1000 500 500 200")
            exec("shell input text \"$password\"")
        } else {
            println("Screen is OFF. Sending wakeup command...")
            exec("shell input keyevent KEYCODE_WAKEUP")
            Thread.sleep(500)
            unlock(password) // recursive call (can be improved later)
        }
    }

    // --- Text Input ---
    fun sendText(message: String) {
        val formatted = formatMessage(message)
        exec("shell input text \"$formatted\"")
    }

    fun sendEnter() = exec("shell input keyevent KEYCODE_ENTER")
    fun sendKey(keyCode: Int) = exec("shell input keyevent $keyCode")

    // --- Navigation / System UI ---
    fun homeButton() = exec("shell am start -W -c android.intent.category.HOME -a android.intent.action.MAIN")
    fun backButton() = exec("shell input keyevent KEYCODE_BACK")
    fun recentButton() = exec("shell am start -n com.android.systemui/com.android.systemui.recents.RecentsActivity")
    fun switchApp() = exec("shell input keyevent KEYCODE_APP_SWITCH")

    fun nextButton() = exec("shell input keyevent 22")
    fun prevButton() = exec("shell input keyevent 21")
    fun upButton() = exec("shell input keyevent 19")
    fun downButton() = exec("shell input keyevent 20")

    fun backspaceButton() = exec("shell input keyevent 67")
    fun tabButton() = exec("shell input keyevent 61")
    fun shiftTab() = exec("shell input keycombination 59 61")
    fun selectAll() = exec("shell input keycombination 113 29")

    fun touchInput(x: Int, y: Int) = exec("shell input tap $x $y")

    fun holdInputTime(
        startX: Int,
        startY: Int,
        endX: Int? = startX,
        endY: Int? = startY,
        time: Int? = 500
    ) = exec("shell input swipe $startX $startY $endX $endY $time")

    fun swipeUp() = holdInputTime(500, 2000, 500, 500, 100)
    fun swipeDown() = holdInputTime(500, 500, 500, 2000, 100)

    // --- APK / App Management ---
    fun install(apk: String): String {
        val install = exec("install $apk")
        return install.inputStream.bufferedReader().readText()
    }

    fun openApp(packageName: String, activityName: String = ""): String {
        val cmd = if (activityName.isNotEmpty()) {
            "shell am start -n $packageName/.$activityName"
        } else {
            "shell am start $packageName"
        }

        val open = exec(cmd)
        return open.inputStream.bufferedReader().readText()
    }

    fun forceClose() {
        switchApp()
        holdInputTime(500, 1000, endY = 100, time = 100)
    }

    fun activityManager() = exec("shell am start -a android.intent.action.VIEW")

    // --- Logcat ---
    fun logCat(): String {
        val read = exec("shell logcat").inputStream.bufferedReader().readText()
        println("logcat $read")
        return read
    }

    // --- Private Helpers ---
    private fun exec(command: String, waitAfter: Long = 100): Process {
        val commandParts = mutableListOf<String>()
        commandParts.add(adbExecutablePath)

        selectedDevice?.let { deviceId ->
            if (command != "devices") {
                commandParts.add("-s")
                commandParts.add(deviceId)
            }
        }

        if (command.startsWith("install ")) {
            val parts = command.split(" ", limit = 2)
            commandParts.addAll(parts)
        } else {
            commandParts.addAll(command.split(" "))
        }

        println("DEBUG: Executing command parts: $commandParts")

        val processBuilder = ProcessBuilder(commandParts)

        val environment = processBuilder.environment()
        val userHome = System.getProperty("user.home")
        environment["HOME"] = userHome
        environment["ANDROID_SDK_HOME"] = "$userHome/.android"

        val process = processBuilder.redirectErrorStream(true).start()
        val finished = process.waitFor(5, TimeUnit.SECONDS)

        if (!finished) {
            println("DEBUG: Process timed out. Forcibly destroying.")
            process.destroyForcibly()
        }

        Thread.sleep(waitAfter)
        return process
    }

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