package id.neotica.modernadb.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation
import id.neotica.modernadb.adb.android.AdbInput
import id.neotica.modernadb.adb.idiomaticAdbInputs
import id.neotica.modernadb.presentation.components.ButtonBasic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ControlsView() {
    var input by remember { mutableStateOf("") }
    var eventMessages by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column {
        Text("Output:")
        Text(eventMessages)

        Row {
            ButtonBasic("Write") {
                scope.launch(Dispatchers.IO) {
                    idiomaticAdbInputs("w $input"){ eventMessages = it }
                    input = ""
                    idiomaticAdbInputs("bs")
                }
            }

            ButtonBasic("Command") {
                scope.launch(Dispatchers.IO) {
                    idiomaticAdbInputs(input){ eventMessages = it }
                    input = ""
                }
            }
        }

        ButtonBasic("Power Button") {
            scope.launch(Dispatchers.IO) {
                idiomaticAdbInputs("power")
            }
        }

        ButtonBasic("Back") {
            scope.launch { idiomaticAdbInputs("back") }
        }

        var password by remember { mutableStateOf("") }

        TextField(
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation()
        )

        ButtonBasic("Unlock device") {
            scope.launch {
                AdbInput.unlock(password)
            }
        }

        ButtonBasic("Swipe Up") {
            scope.launch(Dispatchers.IO) {
                idiomaticAdbInputs("sup")
            }
        }

        ButtonBasic("Swipe Down") {
            scope.launch(Dispatchers.IO) {
                idiomaticAdbInputs("sdown")
            }
        }

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

        ButtonBasic("Home") {
            scope.launch(Dispatchers.IO) {
                idiomaticAdbInputs("home")
            }
        }
    }
}