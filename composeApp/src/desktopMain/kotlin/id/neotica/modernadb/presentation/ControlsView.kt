package id.neotica.modernadb.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import id.neotica.modernadb.adb.android.AdbInput
import id.neotica.modernadb.presentation.components.ButtonBasic
import id.neotica.modernadb.presentation.components.NeoIcon
import id.neotica.modernadb.res.MR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ControlsView(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    val deviceList = listOf("Pixel 6", "Pixel 3")
    var selectedDevice by remember { mutableStateOf(deviceList.first()) }
    var dropDownState by remember { mutableStateOf(false) }

    val unlockMethods = listOf("Password", "Pin")
    var selectedMethod by remember { mutableStateOf(unlockMethods.first()) }
    var unlockState by remember { mutableStateOf(false) }
    Card {
        Column(
            modifier = modifier
                .padding(16.dp),
        ) {
            NeoIcon(
                desc = "Power Button",
                image = MR.images.ic_power,
                onLongClick = { AdbInput.longPressPowerButton() }
            ) {
                AdbInput.powerButton()
            }


            var password by remember { mutableStateOf("") }

            TextField(
                value = password,
                onValueChange = { password = it },
                visualTransformation = PasswordVisualTransformation(),
                keyboardActions = KeyboardActions {
                    scope.launch(Dispatchers.IO) {
                        unlock(password, selectedDevice, selectedMethod)
                    }
                },
                modifier = Modifier.onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        scope.launch(Dispatchers.IO) {
                            unlock(password, selectedDevice, selectedMethod)
                        }
                        true
                    } else {
                        false
                    }
                }
            )

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ButtonBasic("Unlock") {
                    scope.launch {
                        unlock(password, selectedDevice, selectedMethod)
                    }
                }

                DropdownBasic(
                    items = deviceList,
                    selectedItem = selectedDevice,
                    expanded = dropDownState
                ) {
                    selectedDevice = it
                }

                DropdownBasic(
                    items = unlockMethods,
                    selectedItem = selectedMethod,
                    expanded = unlockState
                ) {
                    selectedMethod = it
                }
            }

            var powerState by remember { mutableStateOf(false) }
            ButtonBasic("Power") {
                powerState = !powerState
            }
            if (powerState) {
                ButtonBasic("Shut down") { AdbInput.shutdown() }
                ButtonBasic("Reboot") { AdbInput.reboot() }
                ButtonBasic("Reboot Recovery") { AdbInput.rebootRecovery() }
                ButtonBasic("Reboot Bootloader") { AdbInput.rebootBootloader() }
            }

        }
    }
}

private fun unlock(password: String, device: String, method: String) {

    when (device) {
        "Pixel 6" -> {
            when (method) {
                "Password" -> {}
                "Pin" -> AdbInput.unlock(password)
            }
        }
        else -> {
            when (method) {
                "Password" -> {}
                "Pin" -> AdbInput.unlock(password)
            }
        }
    }

}

@Composable
fun DropdownBasic(
    items: List<String>,
    selectedItem: String,
    expanded: Boolean,
    dropDownStateCallback: (String) -> Unit
) {
    var dropDownState by remember { mutableStateOf(expanded) }
    var selectedDevice by remember { mutableStateOf(selectedItem) }

    Box {
        Row(
            modifier = Modifier.clickable {
                dropDownState = true
            },
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(selectedDevice)
            Icon(
                painter = painterResource(MR.images.ic_expand_more),
                contentDescription = "Expand More"
            )
        }
        DropdownMenu(
            expanded = dropDownState,
            onDismissRequest = {
                dropDownState = false
            }
        ) {
            items.forEach {
                DropdownMenuItem(onClick = {
                    dropDownStateCallback(it)
                    selectedDevice = it
                    dropDownState = false
                }) {
                    Text(it)
                }
            }
        }
    }
}