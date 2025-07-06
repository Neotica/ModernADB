package id.neotica.modernadb.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Switch
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.InternalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.android.AdbInput
import id.neotica.modernadb.adb.idiomaticAdbInputs
import id.neotica.modernadb.presentation.components.ButtonBasic
import id.neotica.modernadb.presentation.components.NeoIcon
import id.neotica.modernadb.res.MR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(InternalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun AndroidNavigationView() {
    var expanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Card(
        Modifier.padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                NeoIcon(
                    desc = "Back",
                    image = MR.images.nav_back,
                    onClick = {
                        scope.launch { idiomaticAdbInputs("back") }
                    }
                )
                NeoIcon(
                    desc = "Home",
                    image = MR.images.nav_home,
                    onClick = {
                        scope.launch { idiomaticAdbInputs("home") }
                    }
                )
                NeoIcon(
                    desc = "Recent App",
                    image = MR.images.nav_recent,
                    onClick = {
                        scope.launch { idiomaticAdbInputs("switch") }
                    }
                )
            }
            if (expanded) {
                Text(
                    text = "collapse!",
                    textAlign = TextAlign.End,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue,
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                )
                var currentMode by remember { mutableStateOf<ControlMode>(ControlMode.Keys) }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "keys",
                        color = if (currentMode == ControlMode.Keys) Color.Black else Color.Gray
                    )
                    Switch(
                        // The switch is "on" (checked) if the mode is Write.
                        checked = currentMode == ControlMode.Swipe,
                        // When the switch is toggled, update the state to the corresponding mode.
                        onCheckedChange = { isChecked ->
                            currentMode = if (isChecked) ControlMode.Swipe else ControlMode.Keys
                        }
                    )
                    Text(
                        text = "swipe",
                        color = if (currentMode == ControlMode.Swipe) Color.Black else Color.Gray
                    )
                }


                when (currentMode) {
                    is ControlMode.Keys -> ArrowViews()
                    is ControlMode.Swipe -> SwipeViews()
                }
            }
            if (!expanded) {
                Text(
                    text = "expand...",
                    textAlign = TextAlign.End,
                    textDecoration = TextDecoration.Underline,
                    color = Color.Blue,
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                )
            }
        }
    }
}

private sealed class ControlMode() {
    object Keys : ControlMode()
    object Swipe : ControlMode()
}

@Composable
private fun SwipeViews() {
    val scope = rememberCoroutineScope()

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ButtonBasic("Swipe Up") {
            AdbInput.swipeUp()
        }
        ButtonBasic("Swipe Down") {
            AdbInput.swipeDown()
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        ButtonBasic("Swipe Left") {
            scope.launch(Dispatchers.IO) {
                idiomaticAdbInputs("sleft")
            }
        }

        ButtonBasic("Swipe Right") {
            scope.launch(Dispatchers.IO) {
                idiomaticAdbInputs("sright")
            }
        }
    }

}

@Composable
private fun ArrowViews() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ButtonBasic("Up") {
            AdbInput.sendKey(23)
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonBasic("<") {
                AdbInput.sendKey(21)
            }
            ButtonBasic("Down") {
                AdbInput.sendKey(24)
            }
            ButtonBasic(">") {
                AdbInput.sendKey(22)
            }
        }
    }
}