package com.aura.voicechat.ui.kyc

import android.Manifest
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.domain.model.KycStep
import com.aura.voicechat.ui.theme.*

/**
 * KYC Screen - ID Card (front/back) + Selfie verification
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * IMPORTANT: Only ID Card and Selfie - NO utility bills
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycScreen(
    onNavigateBack: () -> Unit,
    onKycComplete: () -> Unit,
    viewModel: KycViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Identity Verification") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCanvas
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkCanvas)
                .padding(16.dp)
        ) {
            // Progress Steps
            KycProgressIndicator(
                currentStep = uiState.currentStep,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Content based on current step
            when (uiState.currentStep) {
                KycStep.ID_CARD_FRONT -> {
                    IdCardCaptureStep(
                        title = "ID Card - Front Side",
                        description = "Take a clear photo of the front of your government-issued ID card",
                        onCapture = { uri -> viewModel.setIdCardFront(uri) },
                        capturedImage = uiState.idCardFrontUri,
                        onContinue = { viewModel.nextStep() }
                    )
                }
                KycStep.ID_CARD_BACK -> {
                    IdCardCaptureStep(
                        title = "ID Card - Back Side",
                        description = "Take a clear photo of the back of your ID card",
                        onCapture = { uri -> viewModel.setIdCardBack(uri) },
                        capturedImage = uiState.idCardBackUri,
                        onContinue = { viewModel.nextStep() }
                    )
                }
                KycStep.SELFIE_CAPTURE -> {
                    SelfieCaptureStep(
                        onCapture = { uri -> viewModel.setSelfie(uri) },
                        capturedImage = uiState.selfieUri,
                        onContinue = { viewModel.nextStep() }
                    )
                }
                KycStep.LIVENESS_CHECK -> {
                    LivenessCheckStep(
                        isChecking = uiState.isCheckingLiveness,
                        passed = uiState.livenessCheckPassed,
                        onStartCheck = { viewModel.performLivenessCheck() },
                        onContinue = { viewModel.nextStep() }
                    )
                }
                KycStep.REVIEW_SUBMIT -> {
                    ReviewSubmitStep(
                        idCardFrontUri = uiState.idCardFrontUri,
                        idCardBackUri = uiState.idCardBackUri,
                        selfieUri = uiState.selfieUri,
                        isSubmitting = uiState.isSubmitting,
                        onSubmit = { viewModel.submitKyc() }
                    )
                }
                KycStep.COMPLETE -> {
                    KycCompleteStep(onDone = onKycComplete)
                }
            }
            
            // Error message
            uiState.error?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = ErrorRed.copy(alpha = 0.2f)
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = ErrorRed
                    )
                }
            }
        }
    }
    
    // Handle completion
    LaunchedEffect(uiState.isComplete) {
        if (uiState.isComplete) {
            onKycComplete()
        }
    }
}

@Composable
private fun KycProgressIndicator(
    currentStep: KycStep,
    modifier: Modifier = Modifier
) {
    val steps = listOf(
        "ID Front" to KycStep.ID_CARD_FRONT,
        "ID Back" to KycStep.ID_CARD_BACK,
        "Selfie" to KycStep.SELFIE_CAPTURE,
        "Liveness" to KycStep.LIVENESS_CHECK,
        "Review" to KycStep.REVIEW_SUBMIT
    )
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        steps.forEachIndexed { index, (label, step) ->
            val isCompleted = currentStep.ordinal > step.ordinal
            val isCurrent = currentStep == step
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(
                            when {
                                isCompleted -> SuccessGreen
                                isCurrent -> AccentMagenta
                                else -> DarkCard
                            },
                            RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text(
                            text = "${index + 1}",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isCurrent || isCompleted) TextPrimary else TextTertiary
                )
            }
            
            if (index < steps.lastIndex) {
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .weight(0.5f)
                        .height(2.dp)
                        .background(if (isCompleted) SuccessGreen else DarkCard)
                )
            }
        }
    }
}

@Composable
private fun IdCardCaptureStep(
    title: String,
    description: String,
    onCapture: (String) -> Unit,
    capturedImage: String?,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Camera preview / captured image placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.6f)
                .clip(RoundedCornerShape(16.dp))
                .background(DarkCard)
                .border(2.dp, Purple80, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (capturedImage != null) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = Purple80,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Position ID card here",
                        color = TextSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Tips
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Tips for a good photo:",
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                TipItem("Ensure good lighting")
                TipItem("Keep the ID flat and clear")
                TipItem("All corners must be visible")
                TipItem("Avoid glare and shadows")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Capture / Continue buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (capturedImage == null) {
                Button(
                    onClick = { onCapture("captured_image_uri") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Capture")
                }
            } else {
                OutlinedButton(
                    onClick = { onCapture("") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Retake")
                }
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text("Continue")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun SelfieCaptureStep(
    onCapture: (String) -> Unit,
    capturedImage: String?,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Take a Selfie",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Make sure your face is clearly visible and well-lit",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Camera preview placeholder
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(RoundedCornerShape(140.dp))
                .background(DarkCard)
                .border(4.dp, AccentCyan, RoundedCornerShape(140.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (capturedImage != null) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(64.dp)
                )
            } else {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.Face,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Position face here",
                        color = TextSecondary
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Tips
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                TipItem("Look directly at the camera")
                TipItem("Keep eyes open")
                TipItem("Remove glasses if possible")
                TipItem("Ensure neutral expression")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Capture / Continue buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (capturedImage == null) {
                Button(
                    onClick = { onCapture("selfie_uri") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Take Selfie")
                }
            } else {
                OutlinedButton(
                    onClick = { onCapture("") },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) {
                    Text("Retake")
                }
                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) {
                    Text("Continue")
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun LivenessCheckStep(
    isChecking: Boolean,
    passed: Boolean?,
    onStartCheck: () -> Unit,
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Liveness Check",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "We need to verify you're a real person",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        when {
            isChecking -> {
                CircularProgressIndicator(
                    color = AccentMagenta,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Checking liveness...",
                    color = TextSecondary
                )
            }
            passed == true -> {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = SuccessGreen,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Liveness check passed!",
                    color = SuccessGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }
            passed == false -> {
                Icon(
                    Icons.Default.Error,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Liveness check failed. Please try again.",
                    color = ErrorRed
                )
            }
            else -> {
                Icon(
                    Icons.Default.Face,
                    contentDescription = null,
                    tint = AccentCyan,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        if (passed == true) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
            ) {
                Text("Continue")
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null)
            }
        } else if (!isChecking) {
            Button(
                onClick = onStartCheck,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta)
            ) {
                Text(if (passed == false) "Try Again" else "Start Check")
            }
        }
    }
}

@Composable
private fun ReviewSubmitStep(
    idCardFrontUri: String?,
    idCardBackUri: String?,
    selfieUri: String?,
    isSubmitting: Boolean,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Review & Submit",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Please review your documents before submitting",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Document summary
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkCard)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DocumentRow(
                    label = "ID Card Front",
                    isCompleted = idCardFrontUri != null
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkSurface
                )
                DocumentRow(
                    label = "ID Card Back",
                    isCompleted = idCardBackUri != null
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkSurface
                )
                DocumentRow(
                    label = "Selfie Photo",
                    isCompleted = selfieUri != null
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = DarkSurface
                )
                DocumentRow(
                    label = "Liveness Check",
                    isCompleted = true
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Note
        Card(
            colors = CardDefaults.cardColors(containerColor = InfoBlue.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = InfoBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Your documents will be reviewed within 24-48 hours. You'll receive a notification once verified.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextPrimary
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onSubmit,
            enabled = !isSubmitting && idCardFrontUri != null && idCardBackUri != null && selfieUri != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentMagenta)
        ) {
            if (isSubmitting) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Submit for Verification")
            }
        }
    }
}

@Composable
private fun KycCompleteStep(onDone: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = SuccessGreen,
            modifier = Modifier.size(120.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Verification Submitted!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Your documents are being reviewed.\nYou'll be notified once verified.",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onDone,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple80)
        ) {
            Text("Done")
        }
    }
}

@Composable
private fun TipItem(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = SuccessGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun DocumentRow(
    label: String,
    isCompleted: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary
        )
        Icon(
            if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isCompleted) SuccessGreen else TextTertiary,
            modifier = Modifier.size(24.dp)
        )
    }
}
