package id.neotica.modernadb.presentation

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Card
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
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.android.AdbInput
import id.neotica.modernadb.presentation.components.NeoIcon
import id.neotica.modernadb.res.MR
import id.neotica.modernadb.utils.toast.ToastManager
import id.neotica.modernadb.utils.toast.ToastDurationType

@Composable
fun DeviceListView() {
    Card {
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
                Text("Device List:")
                Column {
                    if (deviceList.isEmpty()) {
                        Text("No devices found. Connect it first!")
                    }
                    deviceList.forEach {
                        Row {
                            Text(
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
                                Text(
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
    }
}