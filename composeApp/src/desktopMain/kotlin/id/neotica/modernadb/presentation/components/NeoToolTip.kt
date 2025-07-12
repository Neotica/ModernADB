package id.neotica.modernadb.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.presentation.theme.DarkBackgroundV2

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeoToolTip(
    text: String,
    content: @Composable () -> Unit,
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            Text(
                text = text,
                color = Color.White,
                modifier = Modifier
                    .background(DarkBackgroundV2)
                    .padding(4.dp)
            )
        },
        state = rememberTooltipState(),
        modifier = Modifier
    ) {
        content()
    }
}