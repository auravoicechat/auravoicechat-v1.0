package com.aura.voicechat

import android.app.Application
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.s3.AWSS3StoragePlugin
import dagger.hilt.android.HiltAndroidApp

/**
 * Aura Voice Chat Application class
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Main application class with Hilt dependency injection and AWS Amplify.
 */
@HiltAndroidApp
class AuraApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize AWS Amplify
        initializeAmplify()
    }
    
    private fun initializeAmplify() {
        try {
            // Add Cognito Auth plugin
            Amplify.addPlugin(AWSCognitoAuthPlugin())
            
            // Add S3 Storage plugin
            Amplify.addPlugin(AWSS3StoragePlugin())
            
            // Configure Amplify
            Amplify.configure(applicationContext)
            
            Log.i(TAG, "AWS Amplify initialized successfully")
        } catch (error: AmplifyException) {
            Log.e(TAG, "Could not initialize Amplify", error)
        }
    }
    
    companion object {
        private const val TAG = "AuraApplication"
    }
}
