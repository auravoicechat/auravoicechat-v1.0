package com.aura.voicechat.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.components.AuraButton
import com.aura.voicechat.ui.theme.*
import kotlinx.coroutines.delay

/**
 * OTP Verification Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Features 6 individual OTP input boxes with auto-focus and auto-advance.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    phoneNumber: String,
    onNavigateToHome: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: OtpVerificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var otpDigits by remember { mutableStateOf(List(6) { "" }) }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    val snackbarHostState = remember { SnackbarHostState() }
    
    // Auto-focus first box on load
    LaunchedEffect(Unit) {
        delay(300)
        focusRequesters[0].requestFocus()
    }
    
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
    
    // Handle successful verification
    LaunchedEffect(uiState.isVerified) {
        if (uiState.isVerified) {
            onNavigateToHome()
        }
    }
    
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(color = DarkCanvas)
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
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Title and subtitle
                Text(
                    text = "Verify Phone",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Enter the code sent to",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                
                Text(
                    text = phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = AccentCyan
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // 6-digit OTP input boxes
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
                ) {
                    otpDigits.forEachIndexed { index, digit ->
                        OtpDigitBox(
                            value = digit,
                            onValueChange = { newValue ->
                                if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                                    otpDigits = otpDigits.toMutableList().apply {
                                        this[index] = newValue
                                    }
                                    
                                    // Auto-advance to next box
                                    if (newValue.isNotEmpty() && index < 5) {
                                        focusRequesters[index + 1].requestFocus()
                                    }
                                    
                                    // Auto-verify when all 6 digits are entered
                                    if (otpDigits.all { it.isNotEmpty() }) {
                                        val otp = otpDigits.joinToString("")
                                        viewModel.verifyOtp(phoneNumber, otp)
                                    }
                                } else if (newValue.isEmpty() && digit.isNotEmpty()) {
                                    // Handle backspace - move to previous box
                                    otpDigits = otpDigits.toMutableList().apply {
                                        this[index] = ""
                                    }
                                    if (index > 0) {
                                        focusRequesters[index - 1].requestFocus()
                                    }
                                }
                            },
                            focusRequester = focusRequesters[index],
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Verify Button
                com.aura.voicechat.ui.components.AuraButton(
                    text = if (uiState.isLoading) "Verifying..." else "Verify Code",
                    onClick = {
                        val otp = otpDigits.joinToString("")
                        viewModel.verifyOtp(phoneNumber, otp)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = otpDigits.all { it.isNotEmpty() } && !uiState.isLoading
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // Resend Code with countdown
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Didn't receive code? ",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    TextButton(
                        onClick = {
                            viewModel.resendOtp(phoneNumber)
                            // Clear OTP boxes on resend
                            otpDigits = List(6) { "" }
                            focusRequesters[0].requestFocus()
                        },
                        enabled = uiState.resendCooldown == 0 && !uiState.isLoading
                    ) {
                        Text(
                            text = if (uiState.resendCooldown > 0) 
                                "Resend (${uiState.resendCooldown}s)" 
                            else 
                                "Resend Code",
                            color = if (uiState.resendCooldown > 0) TextTertiary else AccentCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
            
            // Loading overlay
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DarkCanvas.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Purple80)
                }
            }
        }
    }
}

/**
 * Single OTP digit input box component
 */
@Composable
fun OtpDigitBox(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }
    
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .aspectRatio(1f)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .background(
                color = DarkSurface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 2.dp,
                color = when {
                    isFocused -> AccentCyan
                    value.isNotEmpty() -> Purple80
                    else -> DarkCard
                },
                shape = RoundedCornerShape(12.dp)
            ),
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        decorationBox = { innerTextField ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (value.isEmpty() && !isFocused) {
                    Text(
                        text = "-",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextTertiary
                    )
                }
                innerTextField()
            }
        }
    )
}
