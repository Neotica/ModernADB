package id.neotica.modernadb.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.presentation.theme.DarkBackgroundV2
import id.neotica.modernadb.presentation.theme.DarkPrimary
import id.neotica.modernadb.presentation.theme.DarkPrimaryTransparent40

@Composable
fun NeoCard(
    isDragging: Boolean? = false,
    dropTarget: DragAndDropTarget? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .then(
                if (dropTarget != null) Modifier
                    .padding(16.dp)
                    .width(300.dp)
                    .dragAndDropTarget(
                    shouldStartDragAndDrop = {true},
                    target = dropTarget
                ) else Modifier
            )
            ,
        colors = CardDefaults.cardColors().copy(
            containerColor = if (isDragging == true) DarkPrimary else DarkBackgroundV2,
            contentColor = Color.White
        ),
        border = BorderStroke(
            width = 2.dp,
            color = Color.White
        )
    ) {
        content()
    }
}

@Composable
fun NeoCardSolid(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors().copy(
            containerColor = DarkPrimaryTransparent40,
            contentColor = Color.White,
        ),
    ) {
        content()

    }
}