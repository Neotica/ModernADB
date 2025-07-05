package id.neotica.modernadb.presentation

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
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
fun MainView() {
    val listState = rememberLazyListState()

    BoxWithConstraints {
        val isWideScreen = maxWidth > 600.dp

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                var eventMessages by remember { mutableStateOf("") }
                val scope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    idiomaticAdbInputs("devices") { eventMessages = it }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(eventMessages)
                    ButtonBasic("Reload") {
                        scope.launch(Dispatchers.IO) {
                            idiomaticAdbInputs("devices") { eventMessages = it }
                        }
                    }
                }

                if (isWideScreen) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CommandView()
                        ControlsView()
                    }
                } else {
                    Column(Modifier.fillMaxSize()) {
                        CommandView()
                        ControlsView()
                    }
                }
            }
        }

        VerticalScrollbar(
            modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
            adapter = rememberScrollbarAdapter(
                scrollState = listState
            )
        )
    }
}