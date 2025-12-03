package com.aura.voicechat.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.aura.voicechat.ui.theme.AccentCyan

/**
 * SpeakingIndicator Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Animated pulsing rings around avatar when user is speaking
 * Uses AccentCyan color with 2-3 expanding circles with fade-out
 */
@Composable
fun SpeakingIndicator(
    isSpeaking: Boolean,
    size: Dp = 60.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        if (isSpeaking) {
            // Animated pulsing rings
            repeat(3) { index ->
                val infiniteTransition = rememberInfiniteTransition(label = "speaking_ring_$index")
                
                val scale by infiniteTransition.animateFloat(
                    initialValue = 0.7f,
                    targetValue = 1.5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1500,
                            delayMillis = index * 300,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "scale_$index"
                )
                
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.8f,
                    targetValue = 0f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1500,
                            delayMillis = index * 300,
                            easing = LinearEasing
                        ),
                        repeatMode = RepeatMode.Restart
                    ),
                    label = "alpha_$index"
                )
                
                Canvas(
                    modifier = Modifier.size(size * 1.6f)
                ) {
                    val radius = (this.size.minDimension / 2) * scale
                    drawCircle(
                        color = AccentCyan.copy(alpha = alpha),
                        radius = radius,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
            }
        }
        
        // Content (avatar)
        content()
    }
}
