package me.rerere.rikkahub.ui.pages.chat

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import me.rerere.rikkahub.ui.pages.orbis.OrbisTheme

/**
 * The history state belongs to the caller: entering appearance must not recreate its filter/list.
 * Order follows the approved preview: 新对话 -> 搜索 -> 导航 -> 工具与玩 -> 对话 -> 外观.
 */
@Composable
internal fun OrbisDrawerPages(
    ownerKey: String,
    drawerOpen: Boolean?,
    modifier: Modifier = Modifier,
    newConversation: @Composable () -> Unit,
    leading: @Composable ColumnScope.() -> Unit = {},
    history: @Composable ColumnScope.() -> Unit,
    appearance: @Composable (onBack: () -> Unit) -> Unit,
) {
    var showAppearance by rememberSaveable(ownerKey) { mutableStateOf(false) }
    val scroll = rememberScrollState()
    val railScroll = rememberScrollState()
    val colors = OrbisTheme.colors
    LaunchedEffect(drawerOpen, ownerKey) { if (drawerOpen == false) showAppearance = false }
    BackHandler(enabled = showAppearance && drawerOpen != false) { showAppearance = false }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        if (showAppearance) {
            TextButton(onClick = { showAppearance = false },
                modifier = Modifier.fillMaxWidth().heightIn(min = 48.dp).testTag("drawer-appearance-back")) {
                Text("‹ 返回会话列表")
            }
            Column(Modifier.fillMaxWidth().weight(1f).verticalScroll(scroll)
                .testTag("drawer-appearance-page"), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                appearance { showAppearance = false }
            }
        } else {
            newConversation()
            // 导航 + 工具与玩：整块限高滚动，给下面的会话列表留出空间。
            Column(
                Modifier.fillMaxWidth().heightIn(max = 300.dp).verticalScroll(railScroll),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) { leading() }
            history()
            OutlinedButton(onClick = { showAppearance = true },
                modifier = Modifier.fillMaxWidth().heightIn(min = 44.dp).testTag("drawer-appearance-open"),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = colors.panel, contentColor = colors.ink)) {
                Text("自定义美化 · 外观")
            }
        }
    }
}
