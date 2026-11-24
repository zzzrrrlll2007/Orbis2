package me.rerere.rikkahub.ui.pages.orbis

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.rerere.rikkahub.ui.theme.GlassTints
import me.rerere.rikkahub.ui.theme.glass.GlassTokens
import me.rerere.rikkahub.ui.theme.glass.glassSurface

/**
 * Zoe 的左侧玻璃抽屉：导航 + 会话 + 搜索 + 自定义美化，全部替代原来的底部 Dock 与底部导航面板。
 *
 * 这里只做展示：条目、路由与回调仍由 [OrbisHost] 提供，保证 `OrbisNavigationItem` 语义不变，
 * 上游新增入口时只改宿主那一个数组即可。
 */
@Composable
fun OrbisGlassDrawer(
    visible: Boolean,
    items: List<OrbisNavigationItem>,
    conversations: List<String>,
    currentLabel: String,
    onClose: () -> Unit,
    onSearch: () -> Unit,
    onNewConversation: () -> Unit,
    onAppearance: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = OrbisTheme.colors
    val dark = androidx.compose.foundation.isSystemInDarkTheme()
    val shape = RoundedCornerShape(topEnd = 28.dp, bottomEnd = 28.dp)

    Box(modifier = modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(),
            exit = fadeOut(),
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(GlassTints.scrim(!dark))
                    .clickable(onClick = onClose),
            )
        }

        AnimatedVisibility(
            visible = visible,
            enter = slideInHorizontally(initialOffsetX = { -it }),
            exit = slideOutHorizontally(targetOffsetX = { -it }),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .widthIn(max = 320.dp)
                    .width(312.dp)
                    .clip(shape)
                    .glassSurface(
                        shape = shape,
                        fillTop = GlassTints.top(!dark),
                        fillBottom = GlassTints.bottom(!dark),
                        borderColor = GlassTints.border(!dark),
                        highlightColor = GlassTints.highlight(!dark),
                        elevation = 0.dp,
                    )
                    .statusBarsPadding()
                    .padding(horizontal = 10.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 8.dp, top = 6.dp, end = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Orbis",
                        modifier = Modifier.weight(1f),
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Normal,
                        color = colors.ink,
                    )
                    Box(
                        Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onClose),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("×", fontSize = 18.sp, color = colors.mutedInk)
                    }
                }

                DrawerAction(
                    label = "搜索对话与内容",
                    onClick = onSearch,
                    muted = true,
                )
                Spacer(Modifier.height(6.dp))
                DrawerAction(
                    label = "+  新对话",
                    onClick = onNewConversation,
                )
                Spacer(Modifier.height(8.dp))

                Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    Text(
                        "导航",
                        fontSize = 10.sp,
                        letterSpacing = 2.sp,
                        color = colors.mutedInk.copy(alpha = .7f),
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 6.dp),
                    )
                    items.forEach { item ->
                        val active = item.title == currentLabel
                        DrawerRow(
                            title = item.title,
                            subtitle = item.subtitle,
                            enabled = item.enabled,
                            active = active,
                            onClick = item.onClick,
                        )
                    }

                    if (conversations.isNotEmpty()) {
                        Text(
                            "对话",
                            fontSize = 10.sp,
                            letterSpacing = 2.sp,
                            color = colors.mutedInk.copy(alpha = .7f),
                            modifier = Modifier.padding(start = 10.dp, top = 16.dp, bottom = 6.dp),
                        )
                        conversations.forEach { title ->
                            Text(
                                text = title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { }
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = colors.mutedInk,
                            )
                        }
                    }

                    Text(
                        "外观",
                        fontSize = 10.sp,
                        letterSpacing = 2.sp,
                        color = colors.mutedInk.copy(alpha = .7f),
                        modifier = Modifier.padding(start = 10.dp, top = 16.dp, bottom = 6.dp),
                    )
                    DrawerRow(
                        title = "自定义美化",
                        subtitle = "主题工作室 · 交给 AI 装扮",
                        enabled = true,
                        active = false,
                        onClick = onAppearance,
                    )
                    Spacer(Modifier.height(16.dp))
                }

                Text(
                    text = "开发中：灰色入口尚未连接真实服务。聊天使用现有会话，不创建副本。",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                    fontSize = 10.sp,
                    lineHeight = 15.sp,
                    color = colors.mutedInk.copy(alpha = .7f),
                )
            }
        }
    }
}

@Composable
private fun DrawerAction(
    label: String,
    onClick: () -> Unit,
    muted: Boolean = false,
) {
    val colors = OrbisTheme.colors
    val dark = androidx.compose.foundation.isSystemInDarkTheme()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 40.dp)
            .clip(RoundedCornerShape(13.dp))
            .glassSurface(
                shape = RoundedCornerShape(13.dp),
                fillTop = GlassTints.top(!dark),
                fillBottom = GlassTints.bottom(!dark),
                borderColor = GlassTints.border(!dark),
                highlightColor = GlassTints.highlight(!dark),
                elevation = 0.dp,
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (muted) colors.mutedInk else colors.ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun DrawerRow(
    title: String,
    subtitle: String,
    enabled: Boolean,
    active: Boolean,
    onClick: () -> Unit,
) {
    val colors = OrbisTheme.colors
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 44.dp)
            .clip(RoundedCornerShape(11.dp))
            .then(if (active) Modifier.background(colors.accent.copy(alpha = .10f)) else Modifier)
            .clickable(enabled = enabled, onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(11.dp),
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 13.5.sp,
                color = if (enabled) colors.ink else colors.mutedInk.copy(alpha = .55f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = colors.mutedInk.copy(alpha = .8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
