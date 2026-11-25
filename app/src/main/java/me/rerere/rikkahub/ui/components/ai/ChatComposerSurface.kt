package me.rerere.rikkahub.ui.components.ai

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import me.rerere.rikkahub.ui.theme.GlassTints
import me.rerere.rikkahub.ui.theme.LocalDarkMode

/** Translucent surfaces must not expose the interior of Android's elevation shadow. */
@Composable
internal fun ChatComposerSurface(
    modifier: Modifier,
    shape: Shape,
    color: Color,
    contentColor: Color,
    border: BorderStroke,
    orbis: Boolean,
    content: @Composable () -> Unit,
) {
    // 预览图的输入条是一整块玻璃：竖向渐变 + 1dp 白边 + 顶部高光 + 投影，
    // 而不是一层实心 panel 色。自定义主题仍然走 ThemeChromeSurface。
    val customTheme = me.rerere.rikkahub.ui.pages.orbis.LocalChatTheme.current
    if (orbis && customTheme == null) {
        val light = !LocalDarkMode.current
        Box(
            modifier = modifier
                .shadow(
                    elevation = 12.dp, shape = shape, clip = false,
                    ambientColor = Color(0xFF464F5C).copy(alpha = .12f),
                    spotColor = Color(0xFF464F5C).copy(alpha = .12f),
                )
                .clip(shape)
                .background(Brush.verticalGradient(listOf(GlassTints.top(light), GlassTints.bottom(light))))
                .border(BorderStroke(1.dp, GlassTints.border(light)), shape),
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) { content() }
        }
        return
    }
    me.rerere.rikkahub.ui.components.message.ThemeChromeSurface(role = "composer", enabled = orbis,
        modifier = modifier, shape = shape, radius = 23, color = color, contentColor = contentColor,
        border = border,
        shadowElevation = if (orbis && color.alpha >= 1f) 6.dp else 0.dp,
        content = content)
}

@Composable
internal fun chatInputFieldColors(): TextFieldColors {
    val theme = me.rerere.rikkahub.ui.pages.orbis.LocalChatTheme.current?.body
    val base = TextFieldDefaults.colors()
    val ink = theme?.let { Color(me.rerere.rikkahub.data.orbis.theme.themeArgb(it.chrome.onComposer ?: it.colors.text)) }
    return base.copy(
    focusedTextColor = ink ?: base.focusedTextColor,
    unfocusedTextColor = ink ?: base.unfocusedTextColor,
    cursorColor = ink ?: base.cursorColor,
    focusedPlaceholderColor = ink?.copy(alpha=.65f) ?: base.focusedPlaceholderColor,
    unfocusedPlaceholderColor = ink?.copy(alpha=.65f) ?: base.unfocusedPlaceholderColor,
    focusedLeadingIconColor = ink ?: base.focusedLeadingIconColor,
    unfocusedLeadingIconColor = ink ?: base.unfocusedLeadingIconColor,
    focusedTrailingIconColor = ink ?: base.focusedTrailingIconColor,
    unfocusedTrailingIconColor = ink ?: base.unfocusedTrailingIconColor,
    unfocusedIndicatorColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent,
    errorIndicatorColor = Color.Transparent,
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    errorContainerColor = Color.Transparent,
)
}
