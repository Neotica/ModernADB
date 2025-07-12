package id.neotica.modernadb.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.neotica.modernadb.data.adb.android.AdbInput
import id.neotica.modernadb.presentation.components.ButtonBasic
import id.neotica.modernadb.presentation.components.NeoCardSolid
import id.neotica.modernadb.presentation.components.NeoIcon
import id.neotica.modernadb.presentation.components.NeoText
import id.neotica.modernadb.presentation.components.NeoTextFieldColor
import id.neotica.modernadb.presentation.theme.DarkPrimary
import id.neotica.modernadb.res.MR
import id.neotica.modernadb.utils.toast.ToastDurationType
import id.neotica.modernadb.utils.toast.ToastManager

@Composable
fun DeviceListView() {
    NeoCardSolid (
        modifier = Modifier.fillMaxWidth()
    ) {
        val deviceList = remember { mutableStateListOf<String>() }
        var selectedDevice by remember { mutableStateOf("") }

        fun grabDevice(deviceList: MutableList<String>) {
            deviceList.clear()
            val devices = AdbInput.getDeviceList()
            deviceList.addAll(devices)
            if (deviceList.isNotEmpty()) {
                selectedDevice = deviceList.first()
                AdbInput.selectedDevice = selectedDevice
            } else {
                ToastManager().showToast("Plug your device first!", ToastDurationType.SHORT)
            }
        }

        LaunchedEffect(Unit) {
            grabDevice(deviceList)
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    NeoText("Device List:")
                    Column {
                        if (deviceList.isEmpty()) {
                            Text("No devices found. Connect it first!")
                        }
                        deviceList.forEach {
                            Row {
                                NeoText(
                                    text = it,
                                    modifier = Modifier.combinedClickable(
                                        onLongClick = {
                                            ToastManager().showToast(it, ToastDurationType.SHORT)
                                        }
                                    ) {
                                        selectedDevice = it
                                        AdbInput.selectedDevice = selectedDevice
                                    }
                                )
                                if (it == selectedDevice) {
                                    NeoText(
                                        text = " (Selected)",
                                        color = Color.Red
                                    )
                                }
                            }

                        }
                    }

                    NeoIcon(
                        desc = "Refresh",
                        image = MR.images.ic_reload,
                        size = 24.dp,
                        onClick = {
                            grabDevice(deviceList)
                        }
                    )
                }
                AboutView()
            }
            var expanded by remember { mutableStateOf(false) }
            var port by remember { mutableStateOf("") }
            Text(
                text = "···",
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                textAlign = TextAlign.Center,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = DarkPrimary
            )
            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    NeoText(
                        text = "Connect via Wifi"
                    )
                    TextField(
                        colors = NeoTextFieldColor(),
                        value = port,
                        onValueChange = {
                            if (!it.contains('\n')) {
                                port = it
                            }
                        },
                    )
                    ButtonBasic("Connect") {
                        val connect = AdbInput.connectWireless(port)
                        println("✨ $connect")
                    }
                }
            }


        }

    }
}