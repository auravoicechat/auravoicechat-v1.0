package com.aura.voicechat.ui.room.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.aura.voicechat.ui.components.GiftAnimationData
import com.aura.voicechat.ui.components.GiftAnimationPlayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Gift Animation Queue Manager
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Manages a queue of gift animations in voice rooms
 * Ensures animations play sequentially without overlap
 * Handles priority for high-value gifts
 */
class GiftAnimationQueue {
    private val _queue = MutableStateFlow<List<GiftAnimationData>>(emptyList())
    val queue: StateFlow<List<GiftAnimationData>> = _queue.asStateFlow()
    
    private val _currentAnimation = MutableStateFlow<GiftAnimationData?>(null)
    val currentAnimation: StateFlow<GiftAnimationData?> = _currentAnimation.asStateFlow()
    
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()
    
    /**
     * Add gift animation to queue
     * High-value gifts (>10000 diamonds) get priority
     */
    fun enqueue(gift: GiftAnimationData) {
        _queue.update { currentQueue ->
            if (gift.value > 10000) {
                // High-value gift - add to front
                listOf(gift) + currentQueue
            } else {
                // Regular gift - add to end
                currentQueue + gift
            }
        }
    }
    
    /**
     * Add multiple gifts (combo)
     */
    fun enqueueCombo(gifts: List<GiftAnimationData>) {
        _queue.update { it + gifts }
    }
    
    /**
     * Process next animation in queue
     */
    suspend fun playNext() {
        if (_isPlaying.value) return
        
        val nextGift = _queue.value.firstOrNull() ?: return
        
        _isPlaying.value = true
        _currentAnimation.value = nextGift
        
        // Remove from queue
        _queue.update { it.drop(1) }
        
        // Wait for animation duration
        delay(nextGift.duration + 300) // Add buffer for transitions
        
        _currentAnimation.value = null
        _isPlaying.value = false
        
        // Auto-play next if queue not empty
        if (_queue.value.isNotEmpty()) {
            playNext()
        }
    }
    
    /**
     * Clear all queued animations
     */
    fun clear() {
        _queue.value = emptyList()
        _currentAnimation.value = null
        _isPlaying.value = false
    }
    
    /**
     * Skip current animation
     */
    fun skipCurrent() {
        _currentAnimation.value = null
        _isPlaying.value = false
    }
}

/**
 * Composable for displaying gift animation queue in a room
 */
@Composable
fun GiftAnimationQueueDisplay(
    queue: GiftAnimationQueue,
    modifier: Modifier = Modifier
) {
    val currentAnimation by queue.currentAnimation.collectAsState()
    val isPlaying by queue.isPlaying.collectAsState()
    val queueList by queue.queue.collectAsState()
    
    // Auto-play next animation
    LaunchedEffect(isPlaying, queueList.size) {
        if (!isPlaying && queueList.isNotEmpty()) {
            queue.playNext()
        }
    }
    
    Box(modifier = modifier.fillMaxSize()) {
        currentAnimation?.let { gift ->
            GiftAnimationPlayer(
                gift = gift,
                onAnimationEnd = {
                    // Animation completed, will auto-play next
                }
            )
        }
    }
}

/**
 * Room-specific gift animation queue
 * Handles gift events in voice rooms
 */
@Composable
fun RoomGiftAnimationQueue(
    modifier: Modifier = Modifier
) {
    val queue = remember { GiftAnimationQueue() }
    val scope = rememberCoroutineScope()
    
    // Listen for gift events from room
    // TODO: Connect to room WebSocket/ViewModel for real gift events
    
    GiftAnimationQueueDisplay(
        queue = queue,
        modifier = modifier
    )
}

/**
 * Combo gift detector
 * Groups multiple same gifts sent rapidly
 */
class GiftComboDetector {
    private val comboWindow = 3000L // 3 seconds to be part of combo
    private var lastGift: GiftAnimationData? = null
    private var lastGiftTime = 0L
    private var comboCount = 0
    
    fun process(gift: GiftAnimationData): GiftAnimationData {
        val now = System.currentTimeMillis()
        
        return if (lastGift?.giftName == gift.giftName &&
            lastGift?.senderName == gift.senderName &&
            (now - lastGiftTime) < comboWindow
        ) {
            // Part of combo
            comboCount += gift.quantity
            lastGiftTime = now
            gift.copy(quantity = comboCount)
        } else {
            // New gift or combo ended
            lastGift = gift
            lastGiftTime = now
            comboCount = gift.quantity
            gift
        }
    }
    
    fun reset() {
        lastGift = null
        lastGiftTime = 0L
        comboCount = 0
    }
}

/**
 * Gift animation priority calculator
 */
object GiftAnimationPriority {
    fun calculate(gift: GiftAnimationData): Int {
        return when {
            gift.value >= 100000 -> 5 // Legendary
            gift.value >= 50000 -> 4  // Epic
            gift.value >= 10000 -> 3  // Rare
            gift.value >= 1000 -> 2   // Uncommon
            else -> 1                  // Common
        }
    }
    
    fun shouldInterrupt(newGift: GiftAnimationData, currentGift: GiftAnimationData?): Boolean {
        if (currentGift == null) return true
        
        val newPriority = calculate(newGift)
        val currentPriority = calculate(currentGift)
        
        // Only interrupt if new gift is significantly higher priority
        return newPriority > currentPriority + 1
    }
}

/**
 * Gift animation statistics tracker
 */
class GiftAnimationStats {
    private val _totalGifts = MutableStateFlow(0)
    val totalGifts: StateFlow<Int> = _totalGifts.asStateFlow()
    
    private val _totalValue = MutableStateFlow(0L)
    val totalValue: StateFlow<Long> = _totalValue.asStateFlow()
    
    private val _topSender = MutableStateFlow<String?>(null)
    val topSender: StateFlow<String?> = _topSender.asStateFlow()
    
    private val senderTotals = mutableMapOf<String, Long>()
    
    fun recordGift(gift: GiftAnimationData) {
        _totalGifts.update { it + gift.quantity }
        _totalValue.update { it + (gift.value * gift.quantity) }
        
        senderTotals[gift.senderName] = 
            (senderTotals[gift.senderName] ?: 0) + (gift.value * gift.quantity)
        
        _topSender.value = senderTotals.maxByOrNull { it.value }?.key
    }
    
    fun reset() {
        _totalGifts.value = 0
        _totalValue.value = 0
        _topSender.value = null
        senderTotals.clear()
    }
}
