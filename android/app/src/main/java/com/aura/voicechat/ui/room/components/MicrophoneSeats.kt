package com.aura.voicechat.ui.room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.aura.voicechat.domain.model.MicSeat
import com.aura.voicechat.domain.model.VipTier
import com.aura.voicechat.ui.components.LevelBadge
import com.aura.voicechat.ui.components.SpeakingIndicator
import com.aura.voicechat.ui.components.VipBadge
import com.aura.voicechat.ui.theme.*

/**
 * MicrophoneSeats Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * 8 microphone seats arranged in a grid (2 rows x 4 columns)
 * - Empty seat: Circle with "+" icon, dashed border
 * - Occupied seat: User avatar with speaking animation, mute icon, level badge, username
 */
@Composable
fun MicrophoneSeats(
    seats: List<MicSeat>,
    onSeatClick: (seatIndex: Int) -> Unit,
    onUserClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // First row (seats 0-3)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            seats.take(4).forEach { seat ->
                SeatItem(
                    seat = seat,
                    onSeatClick = { onSeatClick(seat.index) },
                    onUserClick = onUserClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Second row (seats 4-7)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            seats.drop(4).take(4).forEach { seat ->
                SeatItem(
                    seat = seat,
                    onSeatClick = { onSeatClick(seat.index) },
                    onUserClick = onUserClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SeatItem(
    seat: MicSeat,
    onSeatClick: () -> Unit,
    onUserClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (seat.user != null) {
            // Occupied seat
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clickable { onUserClick(seat.user.id) }
            ) {
                SpeakingIndicator(
                    isSpeaking = seat.isSpeaking,
                    size = 70.dp
                ) {
                    Box {
                        // Avatar
                        if (seat.user.avatar != null) {
                            AsyncImage(
                                model = seat.user.avatar,
                                contentDescription = seat.user.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(DarkCard)
                            )
                        } else {
                            // Placeholder avatar
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Purple40),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        
                        // Mute indicator
                        if (seat.isMuted) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(20.dp)
                                    .background(ErrorRed, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.MicOff,
                                    contentDescription = "Muted",
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                        
                        // Level badge
                        LevelBadge(
                            level = seat.user.level,
                            modifier = Modifier.align(Alignment.TopEnd)
                        )
                        
                        // VIP badge
                        val vipTier = when (seat.user.vipTier) {
                            0 -> VipTier.NONE
                            1 -> VipTier.VIP
                            else -> VipTier.SVIP
                        }
                        if (vipTier != VipTier.NONE) {
                            VipBadge(
                                vipTier = vipTier,
                                modifier = Modifier.align(Alignment.TopStart)
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            // Username
            Text(
                text = seat.user.name,
                style = MaterialTheme.typography.labelSmall,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        } else {
            // Empty seat
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .then(
                        if (seat.isLocked) {
                            Modifier.background(DarkCard, CircleShape)
                        } else {
                            Modifier
                                .border(
                                    width = 2.dp,
                                    color = TextTertiary.copy(alpha = 0.3f),
                                    shape = CircleShape
                                )
                                .clickable(onClick = onSeatClick)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (seat.isLocked) Icons.Default.Lock else Icons.Default.Add,
                    contentDescription = if (seat.isLocked) "Locked" else "Join seat",
                    tint = if (seat.isLocked) TextTertiary else AccentMagenta,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = if (seat.isLocked) "Locked" else "Empty",
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary,
                textAlign = TextAlign.Center
            )
        }
    }
}
