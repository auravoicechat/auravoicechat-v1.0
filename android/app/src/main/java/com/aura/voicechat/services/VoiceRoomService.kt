package com.aura.voicechat.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aura.voicechat.R
import com.aura.voicechat.ui.room.RoomActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * Voice Room Foreground Service
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Maintains WebRTC connection for voice/video rooms in the background.
 * Shows persistent notification while user is in a room.
 */
@AndroidEntryPoint
class VoiceRoomService : Service() {
    
    private var roomId: String? = null
    private var isMuted: Boolean = false
    private var isVideoEnabled: Boolean = false
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        Log.i(TAG, "VoiceRoomService created")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                roomId = intent.getStringExtra(EXTRA_ROOM_ID)
                startForegroundWithNotification()
                Log.i(TAG, "Started voice room service for room: $roomId")
            }
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
                Log.i(TAG, "Stopped voice room service")
            }
            ACTION_MUTE -> {
                isMuted = !isMuted
                updateNotification()
                Log.i(TAG, "Mute toggled: $isMuted")
            }
            ACTION_TOGGLE_VIDEO -> {
                isVideoEnabled = !isVideoEnabled
                updateNotification()
                Log.i(TAG, "Video toggled: $isVideoEnabled")
            }
        }
        return START_NOT_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    /**
     * Creates notification channel for Android O+
     */
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Voice room active notification"
            setShowBadge(false)
        }
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
    
    /**
     * Starts foreground service with notification
     */
    private fun startForegroundWithNotification() {
        val notification = createNotification()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MICROPHONE or 
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            )
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }
    
    /**
     * Creates the foreground notification
     */
    private fun createNotification(): Notification {
        // Intent to return to room
        val returnIntent = roomId?.let { id ->
            RoomActivity.createIntent(this, id)
        } ?: Intent()
        
        val pendingIntent = PendingIntent.getActivity(
            this, 0, returnIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        // Mute action
        val muteIntent = Intent(this, VoiceRoomService::class.java).apply {
            action = ACTION_MUTE
        }
        val mutePendingIntent = PendingIntent.getService(
            this, 1, muteIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val muteAction = NotificationCompat.Action.Builder(
            if (isMuted) R.drawable.ic_mic_off else R.drawable.ic_mic_on,
            if (isMuted) "Unmute" else "Mute",
            mutePendingIntent
        ).build()
        
        // Stop action
        val stopIntent = Intent(this, VoiceRoomService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 2, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val stopAction = NotificationCompat.Action.Builder(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Leave Room",
            stopPendingIntent
        ).build()
        
        val statusText = buildString {
            append("Voice Room Active")
            if (isMuted) append(" • Muted")
            if (isVideoEnabled) append(" • Video On")
        }
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Aura Voice Chat")
            .setContentText(statusText)
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .addAction(muteAction)
            .addAction(stopAction)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }
    
    /**
     * Updates notification with current state
     */
    private fun updateNotification() {
        val notification = createNotification()
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up WebRTC resources
        Log.i(TAG, "VoiceRoomService destroyed")
    }
    
    companion object {
        private const val TAG = "VoiceRoomService"
        private const val CHANNEL_ID = "voice_room_channel"
        private const val CHANNEL_NAME = "Voice Room"
        private const val NOTIFICATION_ID = 1001
        
        const val ACTION_START = "com.aura.voicechat.action.START_VOICE_ROOM"
        const val ACTION_STOP = "com.aura.voicechat.action.STOP_VOICE_ROOM"
        const val ACTION_MUTE = "com.aura.voicechat.action.MUTE"
        const val ACTION_TOGGLE_VIDEO = "com.aura.voicechat.action.TOGGLE_VIDEO"
        
        const val EXTRA_ROOM_ID = "room_id"
    }
}
