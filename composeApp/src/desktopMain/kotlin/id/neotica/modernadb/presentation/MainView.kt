package id.neotica.modernadb.presentation

import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainView() {
    val listState = rememberLazyListState()

    BoxWithConstraints(
        Modifier.fillMaxWidth()
    ) {
        val isWideScreen = maxWidth > 600.dp

        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxSize(),
//                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DeviceListView()
                }
                Spacer(Modifier.padding(8.dp))
                if (isWideScreen) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Column {
                            CommandView()
                            Spacer(Modifier.padding(8.dp))
                            AndroidNavigationView()
                        }
                        Spacer(Modifier.padding(8.dp))
                        ControlsView()
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CommandView()
                        AndroidNavigationView()
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