package id.neotica.modernadb.presentation.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun NeoText(
    text: String,
    color: Color = Color.White,
    modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = color,
        modifier = modifier
    )
}