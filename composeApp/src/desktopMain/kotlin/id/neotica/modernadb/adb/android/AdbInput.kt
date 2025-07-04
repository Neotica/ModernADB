package id.neotica.modernadb.adb.android

object AdbInput {

    fun sendText(message: String) {
        val formatted = formatMessage(message)
        exec("adb shell input text \"$formatted\"")
    }

    fun sendEnter() {
        exec("adb shell input keyevent KEYCODE_ENTER")
    }

    fun sendKey(keyCode: Int) {
        exec("adb shell input keyevent $keyCode")
    }

    fun logCat() {
        exec("adb shell logcat")
    }

    fun homeButton() {
        exec("adb shell am start -W -c android.intent.category.HOME -a android.intent.action.MAIN")
    }

    fun backButton() = exec("adb shell input keyevent KEYCODE_BACK" )

    fun recentButton() {
        exec("adb shell am start -n com.android.systemui/com.android.systemui.recents.RecentsActivity")
    }

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
    fun holdInputTime(
        startX: Int, startY: Int, endX: Int? = startX, endY: Int? = startY, time: Int?= 500
    ) = exec("adb shell input swipe $startX $startY $endX $endY $time")

    fun activityManager() {
        exec("adb shell am start -a android.intent.action.VIEW")
    }

    private fun exec(cmd: String, waitAfter: Long = 100): Process {
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
        Thread.sleep(waitAfter) // let input flush to UI
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