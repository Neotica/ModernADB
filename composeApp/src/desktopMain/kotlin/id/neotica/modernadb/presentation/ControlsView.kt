package id.neotica.modernadb.presentation

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import id.neotica.modernadb.data.adb.android.AdbInput
import id.neotica.modernadb.data.adb.idiomaticAdbInputs
import id.neotica.modernadb.presentation.components.ButtonBasic
import id.neotica.modernadb.presentation.components.NeoCard
import id.neotica.modernadb.presentation.components.NeoIcon
import id.neotica.modernadb.presentation.components.NeoTextFieldColor
import id.neotica.modernadb.presentation.components.NeoToolTip
import id.neotica.modernadb.presentation.theme.DarkBackground
import id.neotica.modernadb.res.MR
import id.neotica.modernadb.utils.device.SupportedDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlsView(
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    /** So, this is going to be where you put your own device if you want to contribute.**/
    val deviceList = remember { SupportedDevice.entries.map { it.displayName } }
    var selectedDevice by remember { mutableStateOf(deviceList.first()) }
    var dropDownState by remember { mutableStateOf(false) }

    val unlockMethods = listOf("Pin", "Password")
    var selectedMethod by remember { mutableStateOf(unlockMethods.first()) }
    var unlockState by remember { mutableStateOf(false) }
    NeoCard() {
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
                colors = NeoTextFieldColor(),
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

                NeoToolTip("Unlocking device is currently experimental.") {
                    ButtonBasic("Unlock") {
                        scope.launch {
                            unlock(password, selectedDevice, selectedMethod)
                        }
                    }
                }


                Column {
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

            }

            var powerState by remember { mutableStateOf(false) }
            ButtonBasic("Power") {
                powerState = !powerState
            }
            if (powerState) {
                ButtonBasic("Shut down") { AdbInput.shutdown() }
                ButtonBasic("Shut down Legacy") { AdbInput.shutdownLegacy() }
                ButtonBasic("Reboot") { AdbInput.reboot() }
                ButtonBasic("Reboot Recovery") { AdbInput.rebootRecovery() }
                ButtonBasic("Reboot Bootloader") { AdbInput.rebootBootloader() }
            }

        }
    }
}

private fun unlock(password: String, device: String, method: String) {

    when (device) {
        SupportedDevice.PIXEL_6.displayName -> {
            when (method) {
                "Password" -> {
                    idiomaticAdbInputs("w $password")
                }
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
    /** I realize that this is antipattern method when i hoist the state under the child composable,
     * but well; its only used by one and only one use case; so be it. **/
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
                contentDescription = "Expand More",
                tint = Color.White
            )
        }
        DropdownMenu(
            modifier = Modifier.background(DarkBackground),
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