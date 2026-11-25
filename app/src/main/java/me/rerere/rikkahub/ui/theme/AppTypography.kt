package me.rerere.rikkahub.ui.theme

import androidx.compose.material3.Typography as M3Typography
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp

/**
 * Three font modes exposed as a setting. Sizes are identical across modes on purpose:
 * switching the face must never reflow the conversation.
 *
 * CJK coverage: Android falls back per glyph to the system family when the selected
 * face lacks a character (Minikin), so rare characters degrade gracefully instead of
 * rendering tofu. Bundling LXGW WenKai later only changes [familyFor], see the note there.
 */
enum class TypefaceMode {
    /** Default: elegant, watercolor-friendly. */
    HANDWRITING,
    /** Sober serif for long reading sessions. */
    SONG,
    /** Zero-size fallback: whatever the ROM ships. */
    SYSTEM,
}

/** Provided by the settings layer; defaults to [TypefaceMode.HANDWRITING]. */
val LocalTypefaceMode = compositionLocalOf { TypefaceMode.HANDWRITING }

@OptIn(ExperimentalTextApi::class)
private val MixedScriptLine = LineHeightStyle(
    alignment = LineHeightStyle.Alignment.Center,
    trim = LineHeightStyle.Trim.None,
)

private fun familyFor(mode: TypefaceMode): FontFamily = when (mode) {
    // 预览图的正文是「宋体 / Noto Serif CJK」这一类阅读字体。
    // FontFamily.Cursive 在多数国产 ROM 上并没有中文手写体，会静默退化，看起来像"没改"，
    // 所以这里显式用 Serif，保证任何设备上正文都是同一套阅读字形。
    TypefaceMode.HANDWRITING -> FontFamily.Serif
    TypefaceMode.SONG -> FontFamily.Serif
    TypefaceMode.SYSTEM -> FontFamily.Default
}

@OptIn(ExperimentalTextApi::class)
fun appTypography(mode: TypefaceMode = TypefaceMode.HANDWRITING): M3Typography {
    val base = M3Typography()
    val family = familyFor(mode)

    fun TextStyle.chat(size: Float, lineHeight: Float, weight: FontWeight = FontWeight.Normal) =
        copy(
            fontFamily = family,
            fontSize = size.sp,
            lineHeight = lineHeight.sp,
            fontWeight = weight,
            letterSpacing = 0.2.sp,
            lineHeightStyle = MixedScriptLine,
        )

    // 预览图的 UI 文案（顶栏小字、导航副标题、按钮、chip）用的是 Inter 一类无衬线，
    // 只有正文才是阅读宋体。这里把 label/title 收回系统无衬线，和预览的层级一致。
    fun TextStyle.ui(size: Float, lineHeight: Float, weight: FontWeight = FontWeight.Medium) =
        copy(
            fontFamily = FontFamily.Default,
            fontSize = size.sp,
            lineHeight = lineHeight.sp,
            fontWeight = weight,
            letterSpacing = 0.2.sp,
        )

    return base.copy(
        // Chat body: the only size the redesign deliberately grows (15 → 17.5).
        bodyLarge = base.bodyLarge.chat(17.5f, 32f),
        bodyMedium = base.bodyMedium.chat(16f, 27f),
        bodySmall = base.bodySmall.chat(13.5f, 23f),
        labelLarge = base.labelLarge.ui(14f, 20f),
        labelMedium = base.labelMedium.ui(12f, 17f),
        labelSmall = base.labelSmall.ui(11f, 16f),
        titleLarge = base.titleLarge.ui(20f, 28f),
        titleMedium = base.titleMedium.ui(16f, 24f),
    )
}
