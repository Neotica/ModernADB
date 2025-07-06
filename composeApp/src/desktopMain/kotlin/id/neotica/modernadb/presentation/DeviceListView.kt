package id.neotica.modernadb.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.android.AdbInput
import id.neotica.modernadb.presentation.components.NeoIcon
import id.neotica.modernadb.res.MR

@Composable
fun DeviceListView() {
    Card {
        var eventMessages by remember { mutableStateOf("") }

        LaunchedEffect(Unit) {
            eventMessages = AdbInput.deviceList()
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
                Text(eventMessages)
                NeoIcon(
                    desc = "Refresh",
                    image = MR.images.ic_reload,
                    onClick = {
                        eventMessages = AdbInput.deviceList()
                    }
                )
            }
            AboutView()

        }
    }
}