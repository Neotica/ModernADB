package id.neotica.modernadb.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.idiomaticAdbInputs
import id.neotica.modernadb.presentation.components.ButtonBasic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AndroidNavigationView() {
    var eventMessages by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    Card {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonBasic("Back") {
                    scope.launch { idiomaticAdbInputs("back") }
                }

                ButtonBasic("Home") {
                    scope.launch(Dispatchers.IO) {
                        idiomaticAdbInputs("home")
                    }
                }

                ButtonBasic("Recent") {
                    scope.launch { idiomaticAdbInputs("switch") }
                }
            }
            if (expanded) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
                }

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
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
                }
            }
            Text(
                text = if (expanded) "collapse!" else  "expand...",
                textAlign = TextAlign.End,
                textDecoration = TextDecoration.Underline,
                color = Color.Blue,
                modifier = Modifier
                    .clickable { expanded = !expanded }
            )
        }
    }
}