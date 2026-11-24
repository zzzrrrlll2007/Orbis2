package me.rerere.rikkahub.ui.theme.glass

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Watercolor glass surface, approximated without a backdrop blur.
 *
 * Compose has no `backdrop-filter`: `Modifier.blur()` blurs the element's own content,
 * not what sits behind it. Until a real solution is adopted (Android 12+ RenderEffect or
 * the Haze library), the look is carried by four cheap layers that already read as
 * frosted glass over the watercolor background:
 *
 *  1. a soft drop shadow to lift the panel off the page,
 *  2. a vertical translucent gradient (brighter at the top),
 *  3. a 1dp light border,
 *  4. a 1dp inner top highlight.
 *
 * Call sites pass palette colors so light/dark and theme packages stay in control.
 */
fun Modifier.glassSurface(
    shape: Shape = RoundedCornerShape(GlassTokens.CardRadius),
    fillTop: Color,
    fillBottom: Color,
    borderColor: Color,
    highlightColor: Color,
    elevation: Dp = 10.dp,
): Modifier = this
    .shadow(elevation, shape, clip = false)
    .clip(shape)
    .background(Brush.verticalGradient(listOf(fillTop, fillBottom)))
    .border(GlassTokens.BorderWidth, borderColor, shape)
    .drawWithContent {
        drawContent()
        val stroke = GlassTokens.BorderWidth.toPx()
        drawLine(
            color = highlightColor,
            start = Offset(stroke * 4f, stroke / 2f),
            end = Offset(size.width - stroke * 4f, stroke / 2f),
            strokeWidth = stroke,
        )
    }
