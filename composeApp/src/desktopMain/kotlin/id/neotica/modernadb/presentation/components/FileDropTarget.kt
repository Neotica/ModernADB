package id.neotica.modernadb.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.awtTransferable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import id.neotica.modernadb.adb.android.AdbInput
import id.neotica.modernadb.utils.toast.ToastDurationType
import id.neotica.modernadb.utils.toast.ToastManager
import java.awt.datatransfer.DataFlavor
import java.io.File

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FileDropTarget() {
    var filePath by remember { mutableStateOf("") }
    var isDragging by remember { mutableStateOf(false) }
    val dropTarget = remember {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                super.onStarted(event)
                isDragging = true
            }

            override fun onEnded(event: DragAndDropEvent) {
                super.onEnded(event)
                isDragging = false
            }
            override fun onDrop(event: DragAndDropEvent): Boolean {
                isDragging = false
                println("Action at the target: ${event.action}")

                val event = event.awtTransferable.let {
                    if (it.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                        val files = it.getTransferData(DataFlavor.javaFileListFlavor) as List<*>
                        (files.firstOrNull() as? File)?.absolutePath
                    }
                    else
                        it.transferDataFlavors.first().humanPresentableName
                }
                if (event.toString().contains(".apk")) {
                    filePath = event.toString()
                } else {
                    ToastManager().showToast("Not an APK file", ToastDurationType.SHORT)
                }
                return false
            }
        }
    }

    Column(
        Modifier
            .background(
                if (isDragging) Color.Red else Color.White
            )
            .dragAndDropTarget(
                shouldStartDragAndDrop = {true},
                target = dropTarget
            )
    ) {
        Box {
            Text(
                text = "Drag and drop a file here",
                color = if (isDragging) Color.Blue else Color.Cyan
            )
        }
        Text(
            text = "File path: $filePath",
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(200.dp)
        )
        Spacer(Modifier.padding(8.dp))
        if (filePath.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ButtonBasic("Install APK") {
                    AdbInput.install(filePath)
                }
                ButtonBasic("Cancel") {
                    filePath = ""
                }
            }

        }

    }
}