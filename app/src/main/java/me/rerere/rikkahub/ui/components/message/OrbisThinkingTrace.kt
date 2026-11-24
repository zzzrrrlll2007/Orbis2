package me.rerere.rikkahub.ui.components.message

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rerere.rikkahub.ui.pages.orbis.OrbisTheme

/**
 * Gemini-style thinking entry: one hairline row in the transcript, never a card.
 *
 * Tapping it is what opens `OrbisThinkingSheet`; nothing expands inline, so the reply stays
 * the only thing competing for the reader's attention.
 */
@Composable
fun OrbisThinkingTrace(
    durationSeconds: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = OrbisTheme.colors
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 9.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
    ) {
        SparkleGlyph(tint = colors.accent, modifier = Modifier.size(14.dp))
        Text(
            text = "思考步骤",
            style = MaterialTheme.typography.labelMedium,
            color = colors.mutedInk,
            maxLines = 1,
        )
        Text(text = "·", style = MaterialTheme.typography.labelMedium, color = colors.mutedInk.copy(alpha = .55f))
        if (durationSeconds != null && durationSeconds > 0) {
            Text(
                text = "${durationSeconds}s",
                style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.5.sp),
                color = colors.mutedInk.copy(alpha = .8f),
            )
        }
        Text(text = "›", style = MaterialTheme.typography.labelMedium, color = colors.mutedInk.copy(alpha = .55f))
    }
}

/** Four-point star, stroked hairline. Kept local so this row has no icon-pack dependency. */
@Composable
internal fun SparkleGlyph(
    tint: Color,
    modifier: Modifier = Modifier,
    filled: Boolean = false,
) {
    Canvas(modifier = modifier) {
        val r = size.minDimension / 2f
        val inner = r * .42f
        val path = Path()
        repeat(8) { index ->
            val angle = Math.toRadians((-90 + index * 45).toDouble())
            val radius = if (index % 2 == 0) r else inner
            val x = center.x + (radius * Math.cos(angle)).toFloat()
            val y = center.y + (radius * Math.sin(angle)).toFloat()
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        if (filled) {
            drawPath(path, tint)
        } else {
            drawPath(path, tint, style = Stroke(width = 1.2.dp.toPx()))
        }
    }
}
