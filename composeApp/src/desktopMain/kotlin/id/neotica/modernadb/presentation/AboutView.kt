package id.neotica.modernadb.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.compose.painterResource
import id.neotica.modernadb.res.MR

@Composable
fun AboutView() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(MR.images.ModernADB),
            contentDescription = "ModernADB Logo",
            modifier = Modifier.width(48.dp)
        )
    }
}