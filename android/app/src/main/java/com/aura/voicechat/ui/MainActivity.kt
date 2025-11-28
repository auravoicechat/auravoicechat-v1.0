package com.aura.voicechat.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.aura.voicechat.ui.navigation.AuraNavHost
import com.aura.voicechat.ui.theme.AuraVoiceChatTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity - Entry point for the Aura Voice Chat app
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            AuraVoiceChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AuraNavHost(navController = navController)
                }
            }
        }
    }
}
