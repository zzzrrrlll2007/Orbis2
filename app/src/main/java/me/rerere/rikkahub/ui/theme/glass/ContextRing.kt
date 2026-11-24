package me.rerere.rikkahub.ui.theme.glass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Hairline progress ring that actually reflects the value.
 *
 * The previous chat footer drew a full circle, so "62%" looked identical to "100%".
 *
 * @param progress 0f..1f, clamped.
 */
@Composable
fun ContextRing(
    progress: Float,
    modifier: Modifier = Modifier,
    diameter: Dp = 28.dp,
    trackColor: Color = MaterialTheme.colorScheme.outlineVariant,
    arcColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    strokeWidth: Dp = 1.6.dp,
) {
    val value = progress.coerceIn(0f, 1f)
    Canvas(modifier = modifier.size(diameter)) {
        val stroke = strokeWidth.toPx()
        val radius = (size.minDimension - stroke) / 2f
        val topLeft = Offset(center.x - radius, center.y - radius)
        val arcSize = Size(radius * 2f, radius * 2f)
        drawCircle(color = trackColor, radius = radius, style = Stroke(stroke))
        if (value > 0f) {
            drawArc(
                color = arcColor,
                startAngle = -90f,
                sweepAngle = 360f * value,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }
    }
}
