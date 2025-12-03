package com.aura.voicechat.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aura.voicechat.R
import com.aura.voicechat.ui.MainActivity
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager
import com.amazonaws.mobileconnectors.pinpoint.targeting.notification.NotificationClient
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * AWS Pinpoint Push Notification Service
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles push notifications using AWS Pinpoint instead of Firebase
 */
@Singleton
class AuraPinpointService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private var pinpointManager: PinpointManager? = null
    
    companion object {
        private const val CHANNEL_ID_DEFAULT = "aura_notifications"
        private const val CHANNEL_ID_MESSAGES = "aura_messages"
        private const val CHANNEL_ID_GIFTS = "aura_gifts"
        private const val CHANNEL_ID_SYSTEM = "aura_system"
    }
    
    /**
     * Initialize AWS Pinpoint
     */
    fun initialize() {
        try {
            val config = PinpointConfiguration(
                context,
                AWSMobileClient.getInstance(),
                context.resources.getString(R.string.aws_pinpoint_app_id)
            )
            
            pinpointManager = PinpointManager(config)
            
            createNotificationChannels()
            
            // Register device token with Pinpoint
            pinpointManager?.notificationClient?.registerGCMDeviceToken()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Get Pinpoint Manager instance
     */
    fun getPinpointManager(): PinpointManager? = pinpointManager
    
    /**
     * Handle incoming push notification
     */
    fun handleNotification(data: Map<String, String>) {
        val type = data["type"] ?: "default"
        val title = data["title"] ?: "Aura Voice Chat"
        val body = data["body"] ?: ""
        val deepLink = data["deepLink"]
        val imageUrl = data["imageUrl"]
        
        showNotification(type, title, body, deepLink, imageUrl)
    }
    
    /**
     * Create notification channels
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_ID_DEFAULT,
                    "General Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "General app notifications"
                    enableVibration(true)
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_ID_MESSAGES,
                    "Messages",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "New message notifications"
                    enableVibration(true)
                    enableLights(true)
                },
                NotificationChannel(
                    CHANNEL_ID_GIFTS,
                    "Gifts",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Gift notifications"
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ID_SYSTEM,
                    "System",
                    NotificationManager.IMPORTANCE_LOW
                ).apply {
                    description = "System notifications"
                }
            )
            
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            channels.forEach { notificationManager.createNotificationChannel(it) }
        }
    }
    
    /**
     * Show notification
     */
    private fun showNotification(
        type: String,
        title: String,
        body: String,
        deepLink: String?,
        imageUrl: String?
    ) {
        val channelId = when (type) {
            "message" -> CHANNEL_ID_MESSAGES
            "gift" -> CHANNEL_ID_GIFTS
            "system" -> CHANNEL_ID_SYSTEM
            else -> CHANNEL_ID_DEFAULT
        }
        
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            deepLink?.let { putExtra("deepLink", it) }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        
        // Add image if available
        imageUrl?.let {
            // Load image and add to notification
            // TODO: Implement image loading with Coil/Glide
        }
        
        val notification = notificationBuilder.build()
        
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
    
    /**
     * Register device token
     */
    fun registerDeviceToken(token: String) {
        try {
            pinpointManager?.notificationClient?.registerGCMDeviceToken(token)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Track event with Pinpoint Analytics
     */
    fun trackEvent(eventType: String, attributes: Map<String, String>? = null) {
        try {
            val event = pinpointManager?.analyticsClient?.createEvent(eventType)
            
            attributes?.forEach { (key, value) ->
                event?.addAttribute(key, value)
            }
            
            pinpointManager?.analyticsClient?.recordEvent(event)
            pinpointManager?.analyticsClient?.submitEvents()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * Update user attributes in Pinpoint
     */
    fun updateUserAttributes(attributes: Map<String, String>) {
        try {
            val endpoint = pinpointManager?.targetingClient?.currentEndpoint()
            
            attributes.forEach { (key, value) ->
                endpoint?.addAttribute(key, listOf(value))
            }
            
            pinpointManager?.targetingClient?.updateEndpointProfile()
            
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
