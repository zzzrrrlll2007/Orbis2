package me.rerere.rikkahub.ui.components.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import me.rerere.rikkahub.ui.pages.orbis.OrbisTheme
import me.rerere.rikkahub.ui.theme.LocalDarkMode
import me.rerere.rikkahub.ui.theme.GlassTints
import me.rerere.rikkahub.ui.theme.glass.glassSurface

/**
 * Single seam for every confirmation dialog in the app: restyling this file restyles them all.
 *
 * Public signature is unchanged on purpose, so no call site has to move. Material's AlertDialog
 * is replaced by a plain [Dialog] plus the shared glass surface, because AlertDialog forces its
 * own container color/shape and cannot carry the 1dp highlight border.
 */
@Composable
fun RikkaConfirmDialog(
    show: Boolean,
    title: String,
    confirmText: String,
    dismissText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    text: @Composable () -> Unit,
) {
    if (!show) {
        return
    }

    val colors = OrbisTheme.colors
    val light = !LocalDarkMode.current
    val shape = RoundedCornerShape(24.dp)

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape)
                .glassSurface(
                    shape = shape,
                    fillTop = GlassTints.top(light),
                    fillBottom = GlassTints.bottom(light),
                    borderColor = GlassTints.border(light),
                    highlightColor = GlassTints.highlight(light),
                    elevation = 18.dp,
                )
                .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 8.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = colors.ink,
            )
            Spacer(Modifier.height(10.dp))
            CompositionLocalProvider(LocalContentColor provides colors.mutedInk) {
                text()
            }
            Spacer(Modifier.height(16.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(colors.border.copy(alpha = .45f)),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onDismiss) {
                    Text(dismissText)
                }
                TextButton(onClick = onConfirm) {
                    Text(confirmText, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
