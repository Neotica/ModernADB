package id.neotica.modernadb.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.idiomaticAdbInputs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CommandView(modifier: Modifier = Modifier) {
    var input by remember { mutableStateOf("") }
    var eventMessages by remember { mutableStateOf("") }
    var currentMode by remember { mutableStateOf<InputMode>(InputMode.Command) }

    val scope = rememberCoroutineScope()

    Card(
        modifier = modifier
    ){
        Column(
            modifier = Modifier
//                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // 2. The Switch logic is now based on the sealed class state.
                Switch(
                    // The switch is "on" (checked) if the mode is Write.
                    checked = currentMode == InputMode.Write,
                    // When the switch is toggled, update the state to the corresponding mode.
                    onCheckedChange = { isChecked ->
                        currentMode = if (isChecked) InputMode.Write else InputMode.Command
                    }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Mode: ${currentMode.input}",
                )
            }
            TextField(
                value = input,
                onValueChange = {
                    if (!it.contains('\n')) {
                        input = it
                    }
                },
                trailingIcon = {
                    Button(
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                idiomaticAdbInputs("sall")
                                idiomaticAdbInputs("bs")
                                input = ""
                            }
                        }){ Text("Clear") }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions {
                    scope.launch(Dispatchers.IO) {
                        idiomaticAdbInputs(
                            when (currentMode) {
                                is InputMode.Command -> input
                                is InputMode.Write -> "w $input"
                            }
                        ){ eventMessages = it }
                        input = ""
                        idiomaticAdbInputs("bs")
                    }
                },
                modifier = Modifier.onKeyEvent { keyEvent ->
                    if (keyEvent.key == Key.Enter) {
                        scope.launch(Dispatchers.IO) {
                            idiomaticAdbInputs(
                                when (currentMode) {
                                    is InputMode.Command -> input
                                    is InputMode.Write -> "w $input"
                                }
                            ){ eventMessages = it }
                            input = ""
                        }
                        true
                    } else {
                        false
                    }
                }
            )
            Text("Output:")
            Text(eventMessages)


        }
    }

}

private sealed class InputMode(val input: String) {
    object Command : InputMode("Command")
    object Write : InputMode("Write")
}