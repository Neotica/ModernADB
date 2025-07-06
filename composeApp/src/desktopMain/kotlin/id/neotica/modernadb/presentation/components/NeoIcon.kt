package id.neotica.modernadb.presentation.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeoIcon(
    desc: String,
    image: ImageResource,
    size: Dp = 24.dp,
    onLongClick: (() -> Unit) = {},
    onClick: () -> Unit
) {
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { Text(desc) },
        state = rememberTooltipState()
    ) {
        Icon(
            painter = painterResource(image),
            contentDescription = desc,
            modifier = Modifier
                .size(size)
                .combinedClickable(
                    onLongClick = {
                        onLongClick()
                    }
                ) {
                    onClick()
                }
        )
    }
}