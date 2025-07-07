package id.neotica.modernadb.utils.toast
import androidx.compose.ui.awt.ComposeWindow
import java.awt.*
import java.awt.geom.RoundRectangle2D
import javax.swing.*

class ToastManager constructor() {
    fun showToast(message: String, toastDurationType: ToastDurationType) {
        val parent = composeWindowProvider.invoke()
        val durationType = when (toastDurationType) {
            ToastDurationType.SHORT -> 2000
            ToastDurationType.LONG -> 5000
        }
        if (parent != null) {
            val toast = JWindow(parent)
            toast.background = Color(0, 0, 0, 0) // Transparent window

            val panel = RoundedPanel(arcWidth = 28, arcHeight = 28)
            val label = JLabel(message)
            label.foreground = Color.WHITE
            label.background = Color.BLACK
            label.isOpaque = false
            label.border = BorderFactory.createEmptyBorder(10, 24, 10, 24)
            panel.add(label)
            toast.contentPane.add(panel)
            toast.pack()

            // Position at bottom center inside the parent window
            val parentBounds = parent.bounds
            val x = parentBounds.x + (parentBounds.width - toast.width) / 2
            val y = parentBounds.y + parentBounds.height - toast.height - 40
            toast.setLocation(x, y)
            toast.isVisible = true

            Timer(durationType) { toast.dispose() }.start()
        } else {
            // Fallback: show dialog if window reference is missing
            JOptionPane.showMessageDialog(null, message)
        }
    }
}

private var composeWindowProvider: () -> ComposeWindow? = {
    null
}

// Utility class for rounded toast background
class RoundedPanel(
    private val arcWidth: Int = 24,
    private val arcHeight: Int = 24,
    private val bgColor: Color = Color(50, 50, 50, 220)
) : JPanel() {
    init {
        isOpaque = false // Allow transparency
        layout = FlowLayout()
    }

    override fun paintComponent(g: Graphics) {
        val g2 = g as Graphics2D
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g2.color = bgColor
        g2.fill(
            RoundRectangle2D.Float(
                0f,
                0f,
                width.toFloat(),
                height.toFloat(),
                arcWidth.toFloat(),
                arcHeight.toFloat()
            )
        )
        super.paintComponent(g)
    }
}