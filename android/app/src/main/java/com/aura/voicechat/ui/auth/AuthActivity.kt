package com.aura.voicechat.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aura.voicechat.ui.theme.AuraVoiceChatTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

/**
 * Authentication Activity for Aura Voice Chat
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Handles user authentication via:
 * - AWS Cognito (Phone OTP)
 * - Google Sign-In
 * - Facebook Login
 */
@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            // Handle Google Sign-In success
            account?.idToken?.let { idToken ->
                handleGoogleSignIn(idToken)
            }
        } catch (e: ApiException) {
            Log.e(TAG, "Google Sign-In failed: ${e.statusCode}", e)
        }
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            AuraVoiceChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Use the existing LoginScreen composable
                    LoginScreen(
                        onNavigateToHome = { finishWithResult(true) },
                        onNavigateToOtp = { phoneNumber -> 
                            Log.i(TAG, "Navigating to OTP for: ${phoneNumber.take(4)}****")
                        }
                    )
                }
            }
        }
    }
    
    /**
     * Initiates Google Sign-In flow
     */
    fun initiateGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
        
        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }
    
    /**
     * Handles successful Google Sign-In
     */
    private fun handleGoogleSignIn(idToken: String) {
        // Use AWS Cognito to federate Google identity
        Log.i(TAG, "Google Sign-In successful, federating with Cognito")
        // Implementation handled by ViewModel/Repository
    }
    
    /**
     * Initiates Facebook Login flow
     */
    fun initiateFacebookSignIn() {
        // Facebook Login handled by Facebook SDK
        Log.i(TAG, "Initiating Facebook Sign-In")
    }
    
    /**
     * Initiates Phone OTP authentication via AWS Cognito
     */
    fun initiatePhoneSignIn(phoneNumber: String) {
        Log.i(TAG, "Initiating Phone Sign-In for: ${phoneNumber.take(4)}****")
        // Implementation handled by ViewModel/Repository using AWS Cognito
    }
    
    /**
     * Finishes activity with authentication result
     */
    private fun finishWithResult(success: Boolean) {
        val resultIntent = Intent().apply {
            putExtra(EXTRA_AUTH_SUCCESS, success)
        }
        setResult(if (success) RESULT_OK else RESULT_CANCELED, resultIntent)
        finish()
    }
    
    companion object {
        private const val TAG = "AuthActivity"
        const val EXTRA_AUTH_SUCCESS = "auth_success"
        // Replace with your actual Google Web Client ID
        private const val GOOGLE_WEB_CLIENT_ID = "YOUR_GOOGLE_WEB_CLIENT_ID"
    }
}
