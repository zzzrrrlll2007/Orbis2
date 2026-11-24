package me.rerere.rikkahub.ui.theme.glass

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Zoe · watercolor glass tokens.
 *
 * Presentation-only constants: nothing here reads or writes application state.
 * Blur is expressed as a token even though Compose has no backdrop-filter; see
 * [glassSurface] for how the visual is approximated today.
 */
object GlassTokens {
    /** Design value from the reference sheet. Used when a real blur is wired (Android 12+ / Haze). */
    val Blur: Dp = 16.dp
    const val Saturate: Float = 1.32f

    val BorderWidth: Dp = 1.dp
    val CardRadius: Dp = 26.dp
    val SheetRadius: Dp = 28.dp
    val PillRadius: Dp = 29.dp
    val ThinkingRadius: Dp = 16.dp
    val ChipRadius: Dp = 14.dp

    val ChatGap: Dp = 24.dp
    val ParagraphGap: Dp = 12.dp
    val GroupGap: Dp = 16.dp
    val PagePadding: Dp = 22.dp
}
