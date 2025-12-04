package com.aura.voicechat.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aura.voicechat.R
import com.aura.voicechat.data.model.Song
import dagger.hilt.android.AndroidEntryPoint

/**
 * Music Service for background playback
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@AndroidEntryPoint
class MusicService : Service() {
    
    private var mediaPlayer: MediaPlayer? = null
    private val binder = MusicBinder()
    private var currentSong: Song? = null
    private var isPlaying = false
    
    private var onPlaybackStateChanged: ((Boolean) -> Unit)? = null
    private var onSongCompleted: (() -> Unit)? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "music_playback"
        
        const val ACTION_PLAY = "com.aura.voicechat.PLAY"
        const val ACTION_PAUSE = "com.aura.voicechat.PAUSE"
        const val ACTION_STOP = "com.aura.voicechat.STOP"
        const val ACTION_NEXT = "com.aura.voicechat.NEXT"
        const val ACTION_PREVIOUS = "com.aura.voicechat.PREVIOUS"
    }
    
    inner class MusicBinder : Binder() {
        fun getService(): MusicService = this@MusicService
    }
    
    override fun onBind(intent: Intent?): IBinder {
        return binder
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initializeMediaPlayer()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let { action ->
            when (action) {
                ACTION_PLAY -> resumePlayback()
                ACTION_PAUSE -> pausePlayback()
                ACTION_STOP -> stopPlayback()
                ACTION_NEXT -> playNext()
                ACTION_PREVIOUS -> playPrevious()
            }
        }
        return START_STICKY
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Music Playback",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Controls for music playback"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun initializeMediaPlayer() {
        mediaPlayer = MediaPlayer().apply {
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()
            )
            setOnCompletionListener {
                onSongCompleted?.invoke()
            }
            setOnErrorListener { _, what, extra ->
                // Handle error
                false
            }
        }
    }
    
    fun playSong(song: Song) {
        currentSong = song
        
        try {
            mediaPlayer?.apply {
                reset()
                setDataSource(song.streamUrl)
                prepareAsync()
                setOnPreparedListener {
                    start()
                    isPlaying = true
                    onPlaybackStateChanged?.invoke(true)
                    startForeground(NOTIFICATION_ID, createNotification())
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    fun pausePlayback() {
        mediaPlayer?.pause()
        isPlaying = false
        onPlaybackStateChanged?.invoke(false)
        updateNotification()
    }
    
    fun resumePlayback() {
        mediaPlayer?.start()
        isPlaying = true
        onPlaybackStateChanged?.invoke(true)
        updateNotification()
    }
    
    fun stopPlayback() {
        mediaPlayer?.stop()
        isPlaying = false
        onPlaybackStateChanged?.invoke(false)
        stopForeground(true)
    }
    
    fun seekTo(position: Long) {
        mediaPlayer?.seekTo(position.toInt())
    }
    
    fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition?.toLong() ?: 0L
    }
    
    fun getDuration(): Long {
        return mediaPlayer?.duration?.toLong() ?: 0L
    }
    
    fun isPlaying(): Boolean = isPlaying
    
    fun getCurrentSong(): Song? = currentSong
    
    fun setPlaybackStateListener(listener: (Boolean) -> Unit) {
        onPlaybackStateChanged = listener
    }
    
    fun setSongCompletedListener(listener: () -> Unit) {
        onSongCompleted = listener
    }
    
    private fun playNext() {
        // TODO: Implement with playlist navigation
        onSongCompleted?.invoke()
    }
    
    private fun playPrevious() {
        // TODO: Implement with playlist navigation
    }
    
    private fun createNotification(): Notification {
        val song = currentSong ?: return createEmptyNotification()
        
        val playPauseIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicService::class.java).apply {
                action = if (isPlaying) ACTION_PAUSE else ACTION_PLAY
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val stopIntent = PendingIntent.getService(
            this,
            0,
            Intent(this, MusicService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(song.title)
            .setContentText(song.artist)
            .setSmallIcon(R.drawable.ic_music_note)
            .addAction(
                if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play_arrow,
                if (isPlaying) "Pause" else "Play",
                playPauseIntent
            )
            .addAction(R.drawable.ic_close, "Stop", stopIntent)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1)
            )
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(isPlaying)
            .build()
    }
    
    private fun createEmptyNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("No song playing")
            .setSmallIcon(R.drawable.ic_music_note)
            .build()
    }
    
    private fun updateNotification() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(NOTIFICATION_ID, createNotification())
    }
    
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
