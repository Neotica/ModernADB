package id.neotica.modernadb.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.input.ImeAction
import id.neotica.modernadb.adb.idiomaticAdbInputs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CommandView() {
    var input by remember { mutableStateOf("") }
    var eventMessages by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Column {
        Text("Main commands:")
        TextField(
            value = input,
            onValueChange = {
                if (!it.contains('\n')) {
                    input = it
                }
            },
            trailingIcon = {
                Button(
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
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
                    idiomaticAdbInputs(input){ eventMessages = it }
                    input = ""
                    idiomaticAdbInputs("bs")
                }
            },
            modifier = Modifier.onKeyEvent { keyEvent ->
                if (keyEvent.key == Key.Enter) {
                    scope.launch(Dispatchers.IO) {
                        idiomaticAdbInputs(input){ eventMessages = it }
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