package me.rerere.rikkahub.ui.theme

import androidx.compose.ui.graphics.Color
import me.rerere.rikkahub.ui.pages.orbis.OrbisColors

/**
 * Zoe · Harbor palettes (Slate / Blue / Mist / Stone / Ivory).
 *
 * Reuses the existing [OrbisColors] shape so every Orbis design-system component keeps
 * working unchanged; only the values differ. No new type is introduced on purpose:
 * adding a palette must stay a one-line change in `resolveOrbisVisualColors`.
 */
object ZoePalette {

    val Light = OrbisColors(
        page = Color(0xFFFBF8F3),
        pageTop = Color(0xFFF3EDE4),
        panel = Color(0xFFFFFDF9),
        raisedPanel = Color(0xFFFFFFFF),
        tintedPanel = Color(0xFFEEF2F6),
        ink = Color(0xFF2F3944),
        mutedInk = Color(0xFF5E6A77),
        border = Color(0xFFE3E1DC),
        accent = Color(0xFF64809C),
        onAccent = Color(0xFFFFFFFF),
        sand = Color(0xFFE3D8C9),
        onSand = Color(0xFF6B5744),
        indigo = Color(0xFF5D7891),
        onIndigo = Color(0xFFFFFFFF),
        dock = Color(0xFFF3EDE4),
        onDock = Color(0xFF2F3944),
        homeButton = Color(0xFF4C6173),
        star = Color(0xFF7E98AC),
    )

    val Dark = OrbisColors(
        page = Color(0xFF151A1F),
        pageTop = Color(0xFF1B2129),
        panel = Color(0xFF1E252D),
        raisedPanel = Color(0xFF242C35),
        tintedPanel = Color(0xFF222A33),
        ink = Color(0xFFE9E4DB),
        mutedInk = Color(0xFFA6AFB9),
        border = Color(0xFF3C434C),
        accent = Color(0xFF9DB4C9),
        onAccent = Color(0xFF16202A),
        sand = Color(0xFF3A3428),
        onSand = Color(0xFFD8C6B0),
        indigo = Color(0xFF8FA8BE),
        onIndigo = Color(0xFF12202C),
        dock = Color(0xFF1B2129),
        onDock = Color(0xFFE9E4DB),
        homeButton = Color(0xFF4C6173),
        star = Color(0xFFBCD0E0),
    )

    fun forDark(dark: Boolean): OrbisColors = if (dark) Dark else Light
}

/** Glass fill tokens derived from a palette entry so every surface stays consistent. */
object GlassTints {

    // Compose 没有 backdrop-filter：半透明面板下面就是正文本身，透过去会“脏”。
    // 所以这里把玻璃做成接近不透明的水彩纸白（仍保留渐变、1dp 白边和顶部高光）。
    fun top(light: Boolean): Color = if (light) Color(0xFDFFFFFF) else Color(0xFA5E6A76)
    fun bottom(light: Boolean): Color = if (light) Color(0xF8FBF8F3) else Color(0xF52B333C)
    fun border(light: Boolean): Color = if (light) Color(0xF2FFFFFF) else Color(0x40C6D6E5)
    fun highlight(light: Boolean): Color = if (light) Color(0xFFFFFFFF) else Color(0x1FFFFFFF)
    fun scrim(light: Boolean): Color = if (light) Color(0x44181D22) else Color(0x66000000)
}
