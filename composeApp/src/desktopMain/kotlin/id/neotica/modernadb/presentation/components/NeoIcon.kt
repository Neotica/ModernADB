package id.neotica.modernadb.presentation.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.ImageResource
import dev.icerock.moko.resources.compose.painterResource

@Composable
fun NeoIcon(
    desc: String,
    image: ImageResource,
    size: Dp = 24.dp,
    onLongClick: (() -> Unit) = {},
    onClick: () -> Unit
) {
    NeoToolTip(
        desc
    ) {
        Icon(
            painter = painterResource(image),
            contentDescription = desc,
            tint = Color.White,
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