package com.aura.voicechat.ui.room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aura.voicechat.ui.theme.DarkCard
import com.aura.voicechat.ui.theme.DarkSurface
import com.aura.voicechat.ui.theme.TextPrimary

/**
 * EmojiPanel Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Bottom sheet with emoji grid for quick emoji insertion
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmojiPanel(
    onDismiss: () -> Unit,
    onEmojiSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val emojis = listOf(
        "😀", "😃", "😄", "😁", "😆", "😅", "🤣", "😂",
        "🙂", "🙃", "😉", "😊", "😇", "🥰", "😍", "🤩",
        "😘", "😗", "😚", "😙", "😋", "😛", "😜", "🤪",
        "😝", "🤑", "🤗", "🤭", "🤫", "🤔", "🤐", "🤨",
        "😐", "😑", "😶", "😏", "😒", "🙄", "😬", "🤥",
        "😌", "😔", "😪", "🤤", "😴", "😷", "🤒", "🤕",
        "🤢", "🤮", "🤧", "🥵", "🥶", "🥴", "😵", "🤯",
        "🤠", "🥳", "😎", "🤓", "🧐", "😕", "😟", "🙁",
        "☹️", "😮", "😯", "😲", "😳", "🥺", "😦", "😧",
        "😨", "😰", "😥", "😢", "😭", "😱", "😖", "😣",
        "😞", "😓", "😩", "😫", "🥱", "😤", "😡", "😠",
        "🤬", "😈", "👿", "💀", "☠️", "💩", "🤡", "👹",
        "👺", "👻", "👽", "👾", "🤖", "😺", "😸", "😹",
        "❤️", "💔", "💕", "💖", "💗", "💙", "💚", "💛",
        "🧡", "💜", "🖤", "🤍", "🤎", "💯", "💢", "💥",
        "👍", "👎", "👊", "✊", "🤛", "🤜", "🤝", "👏",
        "🙌", "👐", "🤲", "🙏", "✌️", "🤞", "🤟", "🤘",
        "🎉", "🎊", "🎈", "🎁", "🏆", "🥇", "🥈", "🥉",
        "⭐", "🌟", "✨", "💫", "🔥", "💧", "☀️", "🌙"
    )
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DarkSurface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title
            Text(
                text = "Emojis",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Emoji grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                items(emojis) { emoji ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(DarkCard, RoundedCornerShape(8.dp))
                            .clickable {
                                onEmojiSelected(emoji)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 24.sp
                        )
                    }
                }
            }
        }
    }
}
