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
    // 输入条是一整块玻璃：半透明竖向渐变 + 1px 亮描边 + 轻投影；
    // 底下由 hazeBlur 真正模糊背景，所以这里可以比之前更透一点。
    val customTheme = me.rerere.rikkahub.ui.pages.orbis.LocalChatTheme.current
    if (orbis && customTheme == null) {
        val light = !LocalDarkMode.current
        // 参考「小克Cat 玻璃材质」composer：rgba(surface,.86) -> .72，描边 .46，外圈更亮。
        val fillTop = if (light) Color(0xDBFFFFFF) else Color(0xE65E6A76)
        val fillBottom = if (light) Color(0xB8FBF8F3) else Color(0xD92B333C)
        val stroke = if (light) Color(0xCCFFFFFF) else Color(0x26FFFFFF)
        Box(
            modifier = modifier
                .shadow(
                    elevation = 8.dp, shape = shape, clip = false,
                    ambientColor = Color(0xFF464F5C).copy(alpha = .10f),
                    spotColor = Color(0xFF464F5C).copy(alpha = .05f),
                )
                .clip(shape)
                .background(Brush.verticalGradient(listOf(fillTop, fillBottom)))
                .border(BorderStroke(1.dp, stroke), shape),
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
