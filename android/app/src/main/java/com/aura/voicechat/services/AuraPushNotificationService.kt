package com.aura.voicechat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.aura.voicechat.R
import com.aura.voicechat.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * AWS Pinpoint Push Notification Service
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles push notifications from AWS Pinpoint:
 * - New message notifications
 * - Gift received notifications
 * - Room invite notifications
 * - System announcements
 * - Deep link handling
 * 
 * Note: AWS Amplify Push Notifications plugin handles the FCM token registration
 * and message receiving automatically. This service processes the notification
 * payloads and displays them to the user.
 */
@AndroidEntryPoint
class AuraPushNotificationService : Service() {
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
        Log.i(TAG, "AuraPushNotificationService created")
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { handlePushNotification(it) }
        return START_NOT_STICKY
    }
    
    /**
     * Handles incoming push notification from AWS Pinpoint via Amplify
     */
    private fun handlePushNotification(intent: Intent) {
        val title = intent.getStringExtra(EXTRA_TITLE)
        val body = intent.getStringExtra(EXTRA_BODY)
        val data = intent.extras?.let { bundle ->
            bundle.keySet().associateWith { bundle.getString(it) ?: "" }
        } ?: emptyMap()
        
        if (title != null || body != null) {
            processNotification(title ?: "Aura Voice Chat", body ?: "", data)
        }
    }
    
    /**
     * Processes notification and determines how to display it
     */
    private fun processNotification(title: String, body: String, data: Map<String, String>) {
        val notificationType = data["type"] ?: "general"
        
        when (notificationType) {
            TYPE_NEW_MESSAGE -> handleNewMessage(data)
            TYPE_GIFT_RECEIVED -> handleGiftReceived(data)
            TYPE_ROOM_INVITE -> handleRoomInvite(data)
            TYPE_FOLLOW -> handleNewFollower(data)
            TYPE_CP_REQUEST -> handleCpRequest(data)
            TYPE_SYSTEM -> handleSystemNotification(data)
            else -> showLocalNotification(
                title = title,
                body = body,
                data = data
            )
        }
    }
    
    /**
     * Handles new message notification
     */
    private fun handleNewMessage(data: Map<String, String>) {
        val senderName = data["senderName"] ?: "Someone"
        val preview = data["preview"] ?: "sent you a message"
        showLocalNotification(
            title = senderName,
            body = preview,
            data = data,
            channelId = CHANNEL_MESSAGES
        )
    }
    
    /**
     * Handles gift received notification
     */
    private fun handleGiftReceived(data: Map<String, String>) {
        val senderName = data["senderName"] ?: "Someone"
        val giftName = data["giftName"] ?: "a gift"
        val quantity = data["quantity"]?.toIntOrNull() ?: 1
        
        val body = if (quantity > 1) {
            "$senderName sent you $quantity $giftName"
        } else {
            "$senderName sent you $giftName"
        }
        
        showLocalNotification(
            title = "Gift Received! 🎁",
            body = body,
            data = data,
            channelId = CHANNEL_GIFTS
        )
    }
    
    /**
     * Handles room invite notification
     */
    private fun handleRoomInvite(data: Map<String, String>) {
        val inviterName = data["inviterName"] ?: "Someone"
        val roomName = data["roomName"] ?: "a room"
        showLocalNotification(
            title = "Room Invite",
            body = "$inviterName invited you to join $roomName",
            data = data,
            channelId = CHANNEL_ROOMS
        )
    }
    
    /**
     * Handles new follower notification
     */
    private fun handleNewFollower(data: Map<String, String>) {
        val followerName = data["followerName"] ?: "Someone"
        showLocalNotification(
            title = "New Follower",
            body = "$followerName started following you",
            data = data,
            channelId = CHANNEL_SOCIAL
        )
    }
    
    /**
     * Handles CP request notification
     */
    private fun handleCpRequest(data: Map<String, String>) {
        val requesterName = data["requesterName"] ?: "Someone"
        showLocalNotification(
            title = "CP Request 💕",
            body = "$requesterName wants to be your CP partner",
            data = data,
            channelId = CHANNEL_SOCIAL
        )
    }
    
    /**
     * Handles system notification
     */
    private fun handleSystemNotification(data: Map<String, String>) {
        showLocalNotification(
            title = data["title"] ?: "Aura Voice Chat",
            body = data["body"] ?: "You have a new notification",
            data = data,
            channelId = CHANNEL_SYSTEM
        )
    }
    
    /**
     * Creates notification channels for Android O+
     */
    private fun createNotificationChannels() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        
        val channels = listOf(
            NotificationChannel(
                CHANNEL_MESSAGES, "Messages",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Direct message notifications"
            },
            NotificationChannel(
                CHANNEL_GIFTS, "Gifts",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Gift received notifications"
            },
            NotificationChannel(
                CHANNEL_ROOMS, "Rooms",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Room invite notifications"
            },
            NotificationChannel(
                CHANNEL_SOCIAL, "Social",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Follow, CP, and friend notifications"
            },
            NotificationChannel(
                CHANNEL_SYSTEM, "System",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "System notifications and updates"
            }
        )
        
        channels.forEach { notificationManager.createNotificationChannel(it) }
    }
    
    /**
     * Shows local notification
     */
    private fun showLocalNotification(
        title: String,
        body: String,
        data: Map<String, String>,
        channelId: String = CHANNEL_DEFAULT
    ) {
        // Create deep link intent
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            data.forEach { (key, value) -> putExtra(key, value) }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, System.currentTimeMillis().toInt(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
        
        Log.i(TAG, "Local notification shown: $title")
    }
    
    companion object {
        private const val TAG = "AuraPushService"
        
        // Intent extras
        const val EXTRA_TITLE = "title"
        const val EXTRA_BODY = "body"
        
        // Notification channels
        private const val CHANNEL_DEFAULT = "aura_default"
        private const val CHANNEL_MESSAGES = "aura_messages"
        private const val CHANNEL_GIFTS = "aura_gifts"
        private const val CHANNEL_ROOMS = "aura_rooms"
        private const val CHANNEL_SOCIAL = "aura_social"
        private const val CHANNEL_SYSTEM = "aura_system"
        
        // Notification types
        private const val TYPE_NEW_MESSAGE = "message"
        private const val TYPE_GIFT_RECEIVED = "gift"
        private const val TYPE_ROOM_INVITE = "room_invite"
        private const val TYPE_FOLLOW = "follow"
        private const val TYPE_CP_REQUEST = "cp_request"
        private const val TYPE_SYSTEM = "system"
    }
}
