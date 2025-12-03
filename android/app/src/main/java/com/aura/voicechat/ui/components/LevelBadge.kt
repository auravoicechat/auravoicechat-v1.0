package com.aura.voicechat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * LevelBadge Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Small badge showing user level with gradient background based on level range
 */
@Composable
fun LevelBadge(
    level: Int,
    modifier: Modifier = Modifier
) {
    val gradientColors = when {
        level in 1..20 -> listOf(Color(0xFF808080), Color(0xFF606060)) // Gray
        level in 21..40 -> listOf(Color(0xFF4CAF50), Color(0xFF388E3C)) // Green
        level in 41..60 -> listOf(Color(0xFF2196F3), Color(0xFF1976D2)) // Blue
        level in 61..80 -> listOf(Color(0xFF9C27B0), Color(0xFF7B1FA2)) // Purple
        else -> listOf(Color(0xFFFFD700), Color(0xFFFFA000)) // Gold (81-100+)
    }
    
    Box(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(colors = gradientColors),
                shape = RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 6.dp, vertical = 2.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = level.toString(),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}
