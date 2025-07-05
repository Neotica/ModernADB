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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.idiomaticAdbInputs
import id.neotica.modernadb.presentation.components.ButtonBasic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DeviceListView() {
    Card {
        var eventMessages by remember { mutableStateOf("") }
        val scope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            idiomaticAdbInputs("devices") { eventMessages = it }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
//                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(eventMessages)
                ButtonBasic("Reload") {
                    scope.launch(Dispatchers.IO) {
                        idiomaticAdbInputs("devices") { eventMessages = it }
                    }
                }
            }
            AboutView()

        }
    }
}