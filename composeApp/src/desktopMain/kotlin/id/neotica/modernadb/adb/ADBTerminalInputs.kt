package id.neotica.modernadb.adb

import id.neotica.modernadb.adb.android.AdbInput

fun idiomaticAdbInputs(input: String, callback: ((String) -> Unit)? = null) {
    when {
//            input == "--list" -> println(adbCommands)
        input == "home" -> AdbInput.homeButton()
        input == "recent" -> AdbInput.recentButton()
        input == "back" -> AdbInput.backButton()
        input == "switch" -> AdbInput.switchApp()
        input == "next" || input ==  ">" -> AdbInput.nextButton()
        input == "prev" || input ==  "<" -> AdbInput.prevButton()
        input == "up" -> AdbInput.upButton()
        input == "down" -> AdbInput.downButton()
        input == "enter" -> AdbInput.sendEnter()
        input == "aMan" -> AdbInput.activityManager()
        input == "tab" -> AdbInput.tabButton()
        input == "sall" -> AdbInput.selectAll()
        input == "stab" -> AdbInput.shiftTab()
        input == "bs" -> AdbInput.backspaceButton()
        input == "fc" -> {
            AdbInput.forceClose()
        }
        input == "isAwake" -> {
            val awake = AdbInput.isAwake()
            callback?.invoke(awake.toString())
        }
        input == "power" -> AdbInput.powerButton()
        input == "devices" -> {
            val output = AdbInput.getDevices()//.inputStream.bufferedReader().readText()
            callback?.invoke(output)
        }
        input.startsWith("midtap") -> {
            AdbInput.touchInput(600,1200)
        }
        input.startsWith("sdown") -> {
            AdbInput.swipeDown()
        }
        input.startsWith("sup") -> {
            AdbInput.swipeUp()
        }
        input.startsWith("sright") -> {
            AdbInput.holdInputTime(100 , 1000, endX = 1000, time = 100)
        }
        input.startsWith("sleft") -> {
            AdbInput.holdInputTime(1000 , 1000, endX = 100, time = 100)
        }
        input.startsWith("swipe") -> {
            // command example: "swipe100.300e100.200t2000"
            val coordinates = input.substringAfter("swipe")
            val start = coordinates.substringBefore("e")
            val end = coordinates.substringAfter("e")

            val startX = start.substringBefore(".").toIntOrNull()
            val startY = start.substringAfter(".").toIntOrNull()
            val endX = end.substringBefore(".").toIntOrNull()
            val endY = end.substringAfter(".").substringBefore("t").toIntOrNull()

            val time = coordinates.substringAfter("t").toIntOrNull()
            println("" +
                    "startX: $startX, startY: $startY, endX: $endX, endY: $endY, time: $time")
            println("wait...")
            time?.let { Thread.sleep(time.toLong()) }
            println("finished.")
            if (startX != null && startY != null) {
                AdbInput.holdInputTime(
                    startX = startX,
                    startY = startY,
                    endX = endX,
                    endY = endY,
                    time = time
                )
            } else {
                println("Wrong input.")
            }
        }
        input.startsWith("hold") -> {
            val coordinates = input.substringAfter("hold")
            val x = coordinates.substringBefore(".").toIntOrNull()
            val y = coordinates.substringAfter(".").toIntOrNull()
            val time = 2000
            println("wait...")
            Thread.sleep(time.toLong())
            println("finished.")
            if (x != null && y != null) {
                AdbInput.holdInputTime(x , y, time = time)
            } else {
                println("Wrong input.")
            }
        }
        input.startsWith("tap") -> {
            val coordinates = input.substringAfter("tap")
            val x = coordinates.substringBefore(".").toIntOrNull()
            val y = coordinates.substringAfter(".").toIntOrNull()

            if (x != null && y != null) {
                AdbInput.touchInput(x , y)
            } else {
                println("Wrong input.")
            }
        }
        input.startsWith("tab") -> {
            val tabNumber = input.substringAfter("tab").trim().toIntOrNull()
            if (tabNumber != null) {
                for (i in 1..tabNumber) AdbInput.tabButton()
            } else {
                AdbInput.tabButton()
            }
        }
        input.startsWith("w") -> {
            callback?.invoke("Writing...")
            val writeInput = input.substringAfter("w ")

            AdbInput.sendText(writeInput)
            Thread.sleep(200)
            callback?.invoke("")
        }
        else -> {
            callback?.invoke("nah")
        }
    }
}