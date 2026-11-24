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
    // TODO(fonts): once res/font/lxgw_wenkai_regular.ttf ships, replace this branch with
    // FontFamily(Font(R.font.lxgw_wenkai_regular, FontWeight.Normal), Font(R.font.lxgw_wenkai_medium, FontWeight.Medium))
    // and delete FontFamily.Cursive. Latin glyphs come from the same file, which is what
    // keeps the mixed CJK/Latin baseline aligned; a Latin-only family would fall back to
    // the system sans for Chinese.
    TypefaceMode.HANDWRITING -> FontFamily.Cursive
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

    return base.copy(
        // Chat body: the only size the redesign deliberately grows (15 → 17.5).
        bodyLarge = base.bodyLarge.chat(17.5f, 32f),
        bodyMedium = base.bodyMedium.chat(16f, 27f),
        bodySmall = base.bodySmall.chat(13.5f, 23f),
        labelLarge = base.labelLarge.chat(14f, 20f, FontWeight.Medium),
        labelMedium = base.labelMedium.chat(12f, 17f, FontWeight.Medium),
        labelSmall = base.labelSmall.chat(11f, 16f, FontWeight.Medium),
        titleLarge = base.titleLarge.chat(20f, 28f, FontWeight.Medium),
        titleMedium = base.titleMedium.chat(16f, 24f, FontWeight.Medium),
    )
}
