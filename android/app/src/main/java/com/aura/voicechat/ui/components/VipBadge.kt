package com.aura.voicechat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aura.voicechat.domain.model.VipTier
import com.aura.voicechat.ui.theme.AccentGold
import com.aura.voicechat.ui.theme.Purple80

/**
 * VipBadge Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * VIP crown badge with different colors for VIP and SVIP
 */
@Composable
fun VipBadge(
    vipTier: VipTier,
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 20.dp
) {
    if (vipTier == VipTier.NONE) return
    
    val (backgroundColor, iconColor, hasShadow) = when (vipTier) {
        VipTier.VIP -> Triple(
            Brush.radialGradient(listOf(Purple80, Purple80.copy(alpha = 0.7f))),
            Color.White,
            false
        )
        VipTier.SVIP -> Triple(
            Brush.radialGradient(listOf(AccentGold, AccentGold.copy(alpha = 0.7f))),
            Color.White,
            true
        )
        else -> return
    }
    
    Box(
        modifier = modifier
            .size(size)
            .then(
                if (hasShadow) {
                    Modifier.shadow(4.dp, CircleShape)
                } else {
                    Modifier
                }
            )
            .background(backgroundColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = vipTier.displayName,
            tint = iconColor,
            modifier = Modifier.size(size * 0.6f)
        )
    }
}
