package id.neotica.modernadb.adb

import id.neotica.modernadb.adb.android.AdbInput

fun adbTerminalInputs() {
    while (true) {
        println("Type --list for command list")
        Thread.sleep(500)
        print("Type message to send (or 'exit'): ")
        val input = readlnOrNull() ?: break
        when(input) {
//            "--list" -> println(adbCommands)
            "home" -> AdbInput.homeButton()
            "recent" -> AdbInput.recentButton()
            "back" -> AdbInput.backButton()
            "switch" -> AdbInput.switchApp()
            "next", ">" -> AdbInput.nextButton()
            "prev", "<" -> AdbInput.prevButton()
            "up" -> AdbInput.upButton()
            "down" -> AdbInput.downButton()
            "enter" -> AdbInput.sendEnter()
            "write" -> {
                if (input.lowercase() == "exit") break
                print("Write message: ")
                val writeInput = readlnOrNull() ?: break

                AdbInput.sendText(writeInput)
                Thread.sleep(200)
                AdbInput.sendEnter()
                Thread.sleep(300)
            }
            "aMan" -> AdbInput.activityManager()
            "exit" -> break
            else -> {
                println("nah")
            }
        }
    }
}

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
            AdbInput.switchApp()
            AdbInput.holdInputTime(500 , 1000, endY = 100, time = 100)
        }
//        input == "exit" -> break
        input.startsWith("midtap") -> {
            AdbInput.touchInput(600,1200)
        }
        input.startsWith("sdown") -> {
            AdbInput.holdInputTime(500 , 2000, endY = 500, time = 100)
        }
        input.startsWith("sup") -> {
            AdbInput.holdInputTime(500 , 500, endY = 2000, time = 100)
        }
        input.startsWith("sright") -> {
            AdbInput.holdInputTime(100 , 1000, endX = 1000, time = 100)
        }
        input.startsWith("sleft") -> {
            AdbInput.holdInputTime(1000 , 1000, endX = 100, time = 100)
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
//            if (input.lowercase() == "exit") break
            print("Writing...")
            callback?.invoke("Writing...")
            val writeInput = input.substringAfter("w ")

            AdbInput.sendText(writeInput)
            Thread.sleep(200)
            AdbInput.sendEnter()
            Thread.sleep(300)
            callback?.invoke("")
        }
        else -> {
            callback?.invoke("nah")
        }
    }
}