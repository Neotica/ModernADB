package id.neotica.modernadb.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.TextField
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.android.AdbInput
import id.neotica.modernadb.adb.idiomaticAdbInputs
import id.neotica.modernadb.presentation.components.ButtonBasic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ControlsView(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    Card {
        Column(
            modifier = modifier
                .padding(16.dp),
        ) {
            ButtonBasic("Power Button") {
                scope.launch(Dispatchers.IO) {
                    idiomaticAdbInputs("power")
                }
            }

            var password by remember { mutableStateOf("") }

            TextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardActions = KeyboardActions {
                    scope.launch(Dispatchers.IO) {
                        AdbInput.unlock(password)
                    }
                },
                modifier = Modifier.onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        scope.launch(Dispatchers.IO) {
                            AdbInput.unlock(password)
                        }
                        true
                    } else {
                        false
                    }
                }
            )

            ButtonBasic("Unlock device") {
                scope.launch {
                    AdbInput.unlock(password)
                }
            }
        }
    }
}