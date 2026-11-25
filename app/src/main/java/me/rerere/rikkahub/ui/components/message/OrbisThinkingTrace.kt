package me.rerere.rikkahub.ui.components.message

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rerere.rikkahub.ui.pages.orbis.OrbisTheme
import me.rerere.rikkahub.ui.theme.GlassTints
import me.rerere.rikkahub.ui.theme.LocalDarkMode

/**
 * Gemini-style thinking entry: one glass pill in the transcript, never a card.
 *
 * Tapping it is what opens `OrbisThinkingSheet`; nothing expands inline, so the reply stays
 * the only thing competing for the reader's attention.
 */
@Composable
fun OrbisThinkingTrace(
    durationSeconds: Long?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    stepCount: Int? = null,
) {
    val colors = OrbisTheme.colors
    val light = !LocalDarkMode.current
    val shape = RoundedCornerShape(999.dp)
    Row(
        modifier = modifier
            .shadow(if (light) 4.dp else 0.dp, shape, clip = false)
            .clip(shape)
            // 预览图的 .trace：极淡的玻璃底，让这一行能浮在水彩纸上面而不抢正文。
            .background(
                Brush.verticalGradient(
                    listOf(
                        GlassTints.top(light).copy(alpha = if (light) .62f else .52f),
                        GlassTints.bottom(light).copy(alpha = if (light) .44f else .38f),
                    )
                )
            )
            .border(1.dp, GlassTints.border(light), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 11.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        SparkleGlyph(tint = colors.accent, modifier = Modifier.size(14.dp))
        Text(
            text = "思考步骤",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = colors.ink,
            maxLines = 1,
        )
        if (stepCount != null && stepCount > 0) {
            Text(text = "·", fontSize = 11.sp, color = colors.mutedInk.copy(alpha = .55f))
            Text(
                text = "${stepCount} 步",
                fontSize = 11.sp,
                color = colors.mutedInk,
            )
        }
        if (durationSeconds != null && durationSeconds > 0) {
            Text(text = "·", fontSize = 11.sp, color = colors.mutedInk.copy(alpha = .55f))
            Text(
                text = "${durationSeconds}s",
                fontSize = 11.sp,
                color = colors.mutedInk.copy(alpha = .8f),
            )
        }
        Text(text = "›", fontSize = 12.sp, color = colors.mutedInk.copy(alpha = .6f))
    }
}

/**
 * 对话流里的「一行思考入口」+ 它自己的玻璃抽屉。
 *
 * 状态留在这个 composable 里，调用点（消息分块循环）不需要把状态穿透上去。
 */
@Composable
fun OrbisThinkingTraceSection(
    steps: List<ThinkingStepUi>,
    durationSeconds: Long?,
    modelName: String?,
    modifier: Modifier = Modifier,
) {
    if (steps.isEmpty()) return
    var open by remember { mutableStateOf(false) }
    OrbisThinkingTrace(
        durationSeconds = durationSeconds,
        stepCount = steps.size,
        onClick = { open = true },
        modifier = modifier,
    )
    if (open) OrbisThinkingSheet(
        visible = true,
        steps = steps,
        modelName = modelName,
        onDismiss = { open = false },
    )
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
