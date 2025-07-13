package id.neotica.modernadb

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.icerock.moko.resources.compose.painterResource
import id.neotica.modernadb.presentation.theme.DarkBackground
import id.neotica.modernadb.res.MR

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ModernADB",
        icon = painterResource(MR.images.ModernADB)
    ) {
        Box(
            Modifier.fillMaxSize()
                .background(DarkBackground)
        )
        App()
    }
}