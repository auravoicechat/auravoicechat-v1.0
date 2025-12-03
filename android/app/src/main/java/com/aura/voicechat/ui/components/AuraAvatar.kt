package com.aura.voicechat.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.aura.voicechat.ui.theme.AuraGradient
import com.aura.voicechat.ui.theme.DarkCard
import com.aura.voicechat.ui.theme.TextSecondary

/**
 * Aura Avatar Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Circular avatar with optional frame
 */

@Composable
fun AuraAvatar(
    imageUrl: String?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    showFrame: Boolean = false,
    frameBrush: Brush = AuraGradient
) {
    val avatarSize = if (showFrame) size - 4.dp else size
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        // Frame/border
        if (showFrame) {
            Box(
                modifier = Modifier
                    .size(size)
                    .border(
                        width = 2.dp,
                        brush = frameBrush,
                        shape = CircleShape
                    )
            )
        }

        // Avatar content
        Box(
            modifier = Modifier
                .size(avatarSize)
                .clip(CircleShape)
                .background(DarkCard),
            contentAlignment = Alignment.Center
        ) {
            if (imageUrl.isNullOrBlank()) {
                // Default placeholder icon
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(size / 2),
                    tint = TextSecondary
                )
            } else {
                // Load image using Coil
                Image(
                    painter = rememberAsyncImagePainter(imageUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(avatarSize),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
