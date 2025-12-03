package com.aura.voicechat.data.repository

import com.aura.voicechat.data.local.dao.NotificationDao
import com.aura.voicechat.data.local.entity.NotificationEntity
import com.aura.voicechat.data.model.AppNotification
import com.aura.voicechat.data.model.NotificationType
import com.aura.voicechat.data.remote.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Notification Repository Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@Singleton
class NotificationRepository @Inject constructor(
    private val apiService: ApiService,
    private val notificationDao: NotificationDao
) {
    
    /**
     * Get all notifications from API and cache locally
     */
    fun getNotifications(type: NotificationType? = null): Flow<Result<List<AppNotification>>> = flow {
        try {
            val response = apiService.getNotificationsList(type?.name?.lowercase())
            if (response.isSuccessful && response.body() != null) {
                val notifications = response.body()!!.notifications
                // Cache notifications locally
                notificationDao.insertNotifications(notifications.map { it.toEntity() })
                emit(Result.success(notifications))
            } else {
                // Fallback to cached notifications
                notificationDao.getAllNotifications().collect { entities ->
                    if (entities.isNotEmpty()) {
                        emit(Result.success(entities.map { it.toNotification() }))
                    } else {
                        emit(Result.failure(Exception("Failed to fetch notifications: ${response.message()}")))
                    }
                }
            }
        } catch (e: Exception) {
            // Fallback to cached notifications on exception
            notificationDao.getAllNotifications().collect { entities ->
                if (entities.isNotEmpty()) {
                    emit(Result.success(entities.map { it.toNotification() }))
                } else {
                    emit(Result.failure(e))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Get cached notifications from local database
     */
    fun getCachedNotifications(): Flow<List<AppNotification>> {
        return notificationDao.getAllNotifications()
            .map { entities -> entities.map { it.toNotification() } }
            .flowOn(Dispatchers.IO)
    }
    
    /**
     * Get unread notifications count
     */
    fun getUnreadCount(): Flow<Int> {
        return notificationDao.getUnreadCount()
            .flowOn(Dispatchers.IO)
    }
    
    /**
     * Mark a notification as read
     */
    fun markAsRead(notificationId: String): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.markNotificationAsRead(notificationId)
            if (response.isSuccessful) {
                // Update local cache
                notificationDao.markAsRead(notificationId)
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Failed to mark notification as read: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Mark all notifications as read
     */
    fun markAllAsRead(): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.markAllNotificationsAsRead()
            if (response.isSuccessful) {
                // Update local cache
                notificationDao.markAllAsRead()
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Failed to mark all notifications as read: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    /**
     * Delete a notification
     */
    fun deleteNotification(notificationId: String): Flow<Result<Unit>> = flow {
        try {
            val response = apiService.deleteNotification(notificationId)
            if (response.isSuccessful) {
                // Delete from local cache
                notificationDao.deleteNotificationById(notificationId)
                emit(Result.success(Unit))
            } else {
                emit(Result.failure(Exception("Failed to delete notification: ${response.message()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
    
    // Extension functions for entity mapping
    private fun AppNotification.toEntity() = NotificationEntity(
        id = id,
        type = type.name,
        title = title,
        message = message,
        imageUrl = imageUrl,
        deepLink = deepLink,
        isRead = isRead,
        createdAt = createdAt
    )
    
    private fun NotificationEntity.toNotification() = AppNotification(
        id = id,
        type = NotificationType.valueOf(type.uppercase()),
        title = title,
        message = message,
        imageUrl = imageUrl,
        deepLink = deepLink,
        isRead = isRead,
        createdAt = createdAt
    )
}
