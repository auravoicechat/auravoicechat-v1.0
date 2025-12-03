package com.aura.voicechat.services

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * AudioPermissionHandler
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Utility for requesting and checking microphone permission
 */
object AudioPermissionHandler {
    
    const val REQUEST_CODE_AUDIO = 1001
    
    /**
     * Check if the app has audio recording permission
     */
    fun hasAudioPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    /**
     * Request audio recording permission
     */
    fun requestAudioPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            REQUEST_CODE_AUDIO
        )
    }
    
    /**
     * Check if the app should show permission rationale
     */
    fun shouldShowRationale(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.RECORD_AUDIO
        )
    }
}
