package com.aura.voicechat.ui.room

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aura.voicechat.services.VoiceRoomService
import com.aura.voicechat.ui.theme.AuraVoiceChatTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Voice/Video Room Activity
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles real-time voice/video room functionality with:
 * - WebRTC for audio/video streaming
 * - Real-time room controls
 * - Gift animations
 * - Seat management
 */
@AndroidEntryPoint
class RoomActivity : ComponentActivity() {
    
    private var roomId: String? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        roomId = intent.getStringExtra(EXTRA_ROOM_ID)
        
        if (roomId == null) {
            Log.e(TAG, "Room ID is required")
            finish()
            return
        }
        
        // Start foreground service for background audio
        startVoiceRoomService()
        
        setContent {
            AuraVoiceChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Use the existing RoomScreen composable
                    RoomScreen(
                        roomId = roomId!!,
                        onNavigateBack = { leaveRoom() }
                    )
                }
            }
        }
    }
    
    /**
     * Starts VoiceRoomService for background audio support
     */
    private fun startVoiceRoomService() {
        val serviceIntent = Intent(this, VoiceRoomService::class.java).apply {
            putExtra(VoiceRoomService.EXTRA_ROOM_ID, roomId)
            action = VoiceRoomService.ACTION_START
        }
        startForegroundService(serviceIntent)
        Log.i(TAG, "Voice room service started for room: $roomId")
    }
    
    /**
     * Stops VoiceRoomService when leaving room
     */
    private fun stopVoiceRoomService() {
        val serviceIntent = Intent(this, VoiceRoomService::class.java).apply {
            action = VoiceRoomService.ACTION_STOP
        }
        startService(serviceIntent)
        Log.i(TAG, "Voice room service stopped")
    }
    
    /**
     * Leaves the current room and finishes activity
     */
    private fun leaveRoom() {
        stopVoiceRoomService()
        finish()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing) {
            stopVoiceRoomService()
        }
    }
    
    companion object {
        private const val TAG = "RoomActivity"
        const val EXTRA_ROOM_ID = "room_id"
        
        /**
         * Creates an intent to launch RoomActivity
         */
        fun createIntent(context: Context, roomId: String): Intent {
            return Intent(context, RoomActivity::class.java).apply {
                putExtra(EXTRA_ROOM_ID, roomId)
            }
        }
    }
}
