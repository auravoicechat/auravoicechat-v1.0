package com.aura.voicechat.services

/**
 * AgoraEventHandler Interface
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Callback interface for Agora RTC Engine events
 */
interface AgoraEventHandler {
    /**
     * Called when a remote user joins the channel
     */
    fun onUserJoined(uid: Int)
    
    /**
     * Called when a remote user leaves the channel
     */
    fun onUserOffline(uid: Int)
    
    /**
     * Called when the audio volume of speakers is updated
     */
    fun onAudioVolumeIndication(speakers: Array<AudioVolumeInfo>)
    
    /**
     * Called when an error occurs
     */
    fun onError(error: Int)
}

/**
 * Audio volume information for a speaker
 */
data class AudioVolumeInfo(
    val uid: Int,
    val volume: Int,
    val vad: Int
)
