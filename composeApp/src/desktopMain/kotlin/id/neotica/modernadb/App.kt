package id.neotica.modernadb

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import id.neotica.modernadb.presentation.MainView
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        MainView()
    }
}