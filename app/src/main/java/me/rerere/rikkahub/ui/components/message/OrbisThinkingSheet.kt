package me.rerere.rikkahub.ui.components.message

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rerere.rikkahub.ui.pages.orbis.OrbisTheme
import me.rerere.rikkahub.ui.theme.LocalDarkMode
import me.rerere.rikkahub.ui.theme.glass.GlassTints
import me.rerere.rikkahub.ui.theme.glass.glassSurface

/** One reasoning step, already flattened for display. */
data class ThinkingStepUi(val title: String, val body: String)

/**
 * «思考步骤» bottom sheet: the only place reasoning text is shown.
 *
 * The transcript keeps a single hairline entry row (`OrbisThinkingTrace`); this sheet owns the
 * dotted rail, the per-step title/body and the two footer lines (completion + model).
 * M3's ModalBottomSheet already provides the scrim, drag-to-dismiss and slide animation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrbisThinkingSheet(
    visible: Boolean,
    steps: List<ThinkingStepUi>,
    modelName: String?,
    onDismiss: () -> Unit,
) {
    if (!visible || steps.isEmpty()) return
    val colors = OrbisTheme.colors
    val light = !LocalDarkMode.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = null,
        containerColor = Color.Transparent,
        scrimColor = GlassTints.scrim(light),
        tonalElevation = 0.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .clip(shape)
                .glassSurface(
                    shape = shape,
                    fillTop = GlassTints.top(light),
                    fillBottom = GlassTints.bottom(light),
                    borderColor = GlassTints.border(light),
                    highlightColor = GlassTints.highlight(light),
                    elevation = 0.dp,
                )
                .padding(bottom = 26.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 2.dp),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    Modifier
                        .size(width = 38.dp, height = 4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(colors.border),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(
                    onClick = onDismiss,
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(36.dp),
                ) {
                    Text("×", fontSize = 20.sp, color = colors.mutedInk)
                }
                Text(
                    text = "思考步骤",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.ink,
                )
                Spacer(Modifier.size(36.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(horizontal = 22.dp, vertical = 8.dp),
            ) {
                DottedRail(
                    modifier = Modifier.fillMaxHeight().width(1.dp),
                    color = colors.mutedInk.copy(alpha = .45f),
                )
                Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                    steps.forEachIndexed { index, step ->
                        if (index > 0) Spacer(Modifier.height(18.dp))
                        Text(
                            text = step.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = colors.ink,
                        )
                        Spacer(Modifier.height(5.dp))
                        Text(
                            text = step.body,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.mutedInk,
                            lineHeight = 22.sp,
                        )
                    }
                }
            }

            Spacer(Modifier.height(14.dp))
            Column(
                modifier = Modifier.padding(start = 36.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("✓", color = colors.accent, style = MaterialTheme.typography.labelMedium)
                    Spacer(Modifier.width(8.dp))
                    Text("完成", style = MaterialTheme.typography.labelMedium, color = colors.mutedInk)
                }
                if (!modelName.isNullOrBlank()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SparkleGlyph(tint = colors.accent, modifier = Modifier.size(12.dp), filled = true)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "使用了 $modelName 模型",
                            style = MaterialTheme.typography.labelMedium,
                            color = colors.mutedInk,
                        )
                    }
                }
            }
        }
    }
}

/** Vertical hairline: 1.4dp dot, 3.6dp gap — matches the preview's dotted rail. */
@Composable
private fun DottedRail(modifier: Modifier = Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val dot = 1.4.dp.toPx()
        val gap = 3.6.dp.toPx()
        var y = 0f
        while (y < size.height) {
            drawLine(
                color = color,
                start = Offset(size.width / 2f, y),
                end = Offset(size.width / 2f, minOf(y + dot, size.height)),
                strokeWidth = size.width,
            )
            y += dot + gap
        }
    }
}
