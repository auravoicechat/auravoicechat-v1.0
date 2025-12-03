package com.aura.voicechat.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.components.AuraButton
import com.aura.voicechat.ui.theme.*

/**
 * Phone Login Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Dedicated screen for phone number entry with country code selection.
 * Note: This functionality is already integrated in LoginScreen. 
 * This is a standalone version for navigation flexibility.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneLoginScreen(
    onBack: () -> Unit,
    onSendOtp: (countryCode: String, phoneNumber: String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var phoneNumber by remember { mutableStateOf("") }
    // Using the countries list from LoginScreen (same data)
    val defaultCountry = remember { Country("United States", "US", "+1", "🇺🇸") }
    var selectedCountry by remember { mutableStateOf(defaultCountry) }
    var showCountryPicker by remember { mutableStateOf(false) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Show error snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }
    
    // Note: Country picker removed for simplicity. 
    // Full implementation available in LoginScreen.
    // Using default US country code for this standalone version.
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = com.aura.voicechat.ui.theme.DarkCanvas)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top bar with back button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = com.aura.voicechat.ui.theme.TextPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Title
                Text(
                    text = "Enter Phone Number",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = com.aura.voicechat.ui.theme.TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "We'll send you a verification code",
                    style = MaterialTheme.typography.bodyMedium,
                    color = com.aura.voicechat.ui.theme.TextSecondary
                )
                
                Spacer(modifier = Modifier.height(48.dp))
                
                // Phone Number Input with Country Code Picker
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Country Code Display (fixed to US for standalone version)
                    OutlinedButton(
                        onClick = { /* Country picker disabled in standalone version */ },
                        modifier = Modifier.height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = com.aura.voicechat.ui.theme.DarkCard,
                            contentColor = com.aura.voicechat.ui.theme.TextPrimary
                        ),
                        border = BorderStroke(1.dp, com.aura.voicechat.ui.theme.Purple80),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        enabled = false
                    ) {
                        Text(
                            text = selectedCountry.flag,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = selectedCountry.dialCode,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Select country"
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    // Phone Number Field
                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it.filter { char -> char.isDigit() } },
                        label = { Text("Phone Number") },
                        placeholder = { Text("234 567 8900") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AccentCyan,
                            focusedLabelColor = AccentCyan,
                            focusedContainerColor = DarkCard,
                            unfocusedContainerColor = DarkCard
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Send OTP Button
                AuraButton(
                    text = if (uiState.isLoading) "Sending..." else "Send OTP",
                    onClick = {
                        if (phoneNumber.isNotBlank()) {
                            val fullPhoneNumber = "${selectedCountry.dialCode}$phoneNumber"
                            viewModel.sendOtp(fullPhoneNumber)
                            onSendOtp(selectedCountry.dialCode, phoneNumber)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = phoneNumber.isNotBlank() && !uiState.isLoading
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Terms reminder
                Text(
                    text = "By continuing, you agree to our Terms of Service and Privacy Policy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Loading overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Purple80)
                }
            }
        }
    }
}
