package id.neotica.modernadb.presentation.components

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ButtonBasic(text: String, onClick: () -> Unit) {
    Button(onClick) { Text(text) }
}