package me.rerere.rikkahub.ui.theme.glass

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * 预览图的纸质底：一层竖向纸色 + 三团水彩晕染（左上一团、右上一团、底部一大团）。
 *
 * 这三团就是一整张屏幕「配色」的来源——纸是暖的，晕染是冷蓝和暖沙交替。
 * Compose 没有 backdrop-filter，也不需要：水彩本身就是半透明色层，直接画就对了。
 */
fun Modifier.watercolorWash(light: Boolean): Modifier = drawBehind {
    val w = size.width
    val h = size.height

    // --paper -> --paper-2
    drawRect(
        Brush.verticalGradient(
            listOf(
                if (light) Color(0xFFFBF8F3) else Color(0xFF151A1F),
                if (light) Color(0xFFF3EDE4) else Color(0xFF1B2129),
            ),
        ),
    )

    fun blob(cx: Float, cy: Float, spread: Float, color: Color) {
        drawRect(
            Brush.radialGradient(
                colorStops = arrayOf(
                    0f to color,
                    0.38f to color.copy(alpha = color.alpha * 0.62f),
                    0.70f to Color.Transparent,
                ),
                center = Offset(cx, cy),
                radius = spread,
            ),
        )
    }

    // --w1 rgba(146,175,199,.34) at 8% 2%
    blob(w * 0.08f, h * 0.02f, w * 1.02f,
        if (light) Color(0x5792AFC7) else Color(0x4D607E9B))
    // --w2 rgba(214,196,173,.32) at 92% 12%
    blob(w * 0.92f, h * 0.12f, w * 0.90f,
        if (light) Color(0x52D6C4AD) else Color(0x3D7A6958))
    // --w3 rgba(176,197,212,.26) at 46% 100%
    blob(w * 0.46f, h, w * 1.10f,
        if (light) Color(0x42B0C5D4) else Color(0x424E687C))
}
