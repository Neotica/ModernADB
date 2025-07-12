package id.neotica.modernadb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import id.neotica.modernadb.presentation.theme.DarkBackground

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ModernADB",
//        transparent = true,
//        undecorated = true
    ) {
        Box(
            Modifier.fillMaxSize()
                .background(DarkBackground)
        )
        App()
    }
}