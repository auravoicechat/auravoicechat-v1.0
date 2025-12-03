package com.aura.voicechat.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.aura.voicechat.ui.theme.AccentMagenta
import com.aura.voicechat.ui.theme.Purple40

/**
 * Gift Animation Player
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Plays gift animations with SVGA or Lottie based on gift type
 * Shows sender info, gift name, and value
 */
@Composable
fun GiftAnimationPlayer(
    gift: GiftAnimationData,
    onAnimationEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(true) }
    
    LaunchedEffect(Unit) {
        // Auto-hide after animation duration
        kotlinx.coroutines.delay(gift.duration)
        isVisible = false
        kotlinx.coroutines.delay(300) // Wait for exit animation
        onAnimationEnd()
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { it }),
        exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
    ) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = when (gift.animationType) {
                GiftAnimationType.FULLSCREEN -> Alignment.Center
                GiftAnimationType.BOTTOM -> Alignment.BottomCenter
                GiftAnimationType.TOP -> Alignment.TopCenter
                GiftAnimationType.FLOAT -> Alignment.CenterEnd
            }
        ) {
            when (gift.format) {
                GiftFormat.SVGA -> {
                    // Full-screen SVGA animation
                    if (gift.animationType == GiftAnimationType.FULLSCREEN) {
                        SvgaPlayer(
                            assetName = "svga/${gift.animationAsset}",
                            modifier = Modifier.fillMaxSize(),
                            loops = 1,
                            clearsAfterStop = true,
                            fillMode = FillMode.Forward,
                            onAnimationEnd = { isVisible = false }
                        )
                    } else {
                        // Smaller SVGA with info overlay
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            SvgaPlayer(
                                assetName = "svga/${gift.animationAsset}",
                                modifier = Modifier.size(300.dp),
                                loops = 1,
                                clearsAfterStop = true
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            GiftInfoOverlay(gift)
                        }
                    }
                }
                
                GiftFormat.LOTTIE -> {
                    // Lottie animation
                    if (gift.animationType == GiftAnimationType.FULLSCREEN) {
                        LottiePlayer(
                            assetName = gift.animationAsset,
                            modifier = Modifier.fillMaxSize(),
                            iterations = 1,
                            onAnimationEnd = { isVisible = false }
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            LottiePlayer(
                                assetName = gift.animationAsset,
                                modifier = Modifier.size(300.dp),
                                iterations = 1
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            GiftInfoOverlay(gift)
                        }
                    }
                }
                
                GiftFormat.STATIC -> {
                    // Static image with animation
                    StaticGiftAnimation(gift)
                }
            }
            
            // Overlay info for full-screen animations
            if (gift.animationType == GiftAnimationType.FULLSCREEN) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    GiftInfoOverlay(gift)
                }
            }
        }
    }
}

@Composable
private fun GiftInfoOverlay(gift: GiftAnimationData) {
    Box(
        modifier = Modifier
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.7f),
                        Purple40.copy(alpha = 0.7f),
                        Color.Black.copy(alpha = 0.7f)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Sender Avatar
            AsyncImage(
                model = gift.senderAvatar,
                contentDescription = "Sender",
                modifier = Modifier.size(40.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                // Sender Name
                Text(
                    text = gift.senderName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                // Gift info
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "sent ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Text(
                        text = gift.giftName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = AccentMagenta
                    )
                    
                    gift.recipientName?.let { recipient ->
                        Text(
                            text = " to $recipient",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Gift value
            Text(
                text = "×${gift.quantity}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFD700),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
private fun StaticGiftAnimation(gift: GiftAnimationData) {
    val infiniteTransition = rememberInfiniteTransition(label = "gift_anim")
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        // Animated gift icon
        AsyncImage(
            model = gift.giftIcon,
            contentDescription = gift.giftName,
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        GiftInfoOverlay(gift)
    }
}

// Data classes
data class GiftAnimationData(
    val id: String,
    val giftName: String,
    val giftIcon: String?,
    val senderName: String,
    val senderAvatar: String?,
    val recipientName: String? = null,
    val quantity: Int = 1,
    val value: Long,
    val format: GiftFormat,
    val animationAsset: String,
    val animationType: GiftAnimationType,
    val duration: Long = 3000L // milliseconds
)

enum class GiftFormat {
    SVGA,      // SVGA animation
    LOTTIE,    // Lottie JSON animation
    STATIC     // Static image with Compose animations
}

enum class GiftAnimationType {
    FULLSCREEN,  // Full screen takeover
    BOTTOM,      // Bottom of screen
    TOP,         // Top of screen
    FLOAT        // Floating on the side
}
