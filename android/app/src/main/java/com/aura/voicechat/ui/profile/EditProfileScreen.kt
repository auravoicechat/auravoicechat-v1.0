package com.aura.voicechat.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.aura.voicechat.domain.model.Gender
import com.aura.voicechat.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Edit Profile Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Allows user to edit their profile information including:
 * - Avatar upload (camera/gallery)
 * - Cover image upload
 * - Display name
 * - Bio/About
 * - Gender selection
 * - Birthday picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    var displayName by remember { mutableStateOf(uiState.user?.name ?: "") }
    var bio by remember { mutableStateOf(uiState.user?.bio ?: "") }
    var selectedGender by remember { mutableStateOf(uiState.user?.gender ?: Gender.UNSPECIFIED) }
    var birthday by remember { mutableStateOf(uiState.user?.birthday) }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var coverUri by remember { mutableStateOf<Uri?>(null) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showBirthdayPicker by remember { mutableStateOf(false) }
    var showImageSourceDialog by remember { mutableStateOf(false) }
    var isEditingAvatar by remember { mutableStateOf(false) }
    
    // Image pickers
    val avatarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { avatarUri = it }
    }
    
    val coverLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { coverUri = it }
    }
    
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Handle camera result
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            viewModel.updateProfile(
                                name = displayName,
                                bio = bio,
                                gender = selectedGender,
                                birthday = birthday,
                                avatarUri = avatarUri,
                                coverUri = coverUri
                            )
                            onNavigateBack()
                        },
                        enabled = displayName.isNotBlank()
                    ) {
                        Text("Save", color = AccentMagenta)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkCanvas)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkCanvas)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Cover Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Purple40.copy(alpha = 0.3f))
                    .clickable { coverLauncher.launch("image/*") }
            ) {
                if (coverUri != null) {
                    AsyncImage(
                        model = coverUri,
                        contentDescription = "Cover Image",
                        modifier = Modifier.fillMaxSize()
                    )
                } else if (uiState.user?.coverImage != null) {
                    AsyncImage(
                        model = uiState.user?.coverImage,
                        contentDescription = "Cover Image",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                IconButton(
                    onClick = { coverLauncher.launch("image/*") },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.CameraAlt, "Edit Cover", tint = Color.White)
                }
            }
            
            // Avatar Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-60).dp),
                contentAlignment = Alignment.Center
            ) {
                Box {
                    AsyncImage(
                        model = avatarUri ?: uiState.user?.avatar,
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(4.dp, DarkCanvas, CircleShape)
                            .background(Purple40.copy(alpha = 0.2f))
                            .clickable {
                                isEditingAvatar = true
                                showImageSourceDialog = true
                            }
                    )
                    
                    IconButton(
                        onClick = {
                            isEditingAvatar = true
                            showImageSourceDialog = true
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .background(AccentMagenta, CircleShape)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            "Edit Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Form Fields
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                // Display Name
                OutlinedTextField(
                    value = displayName,
                    onValueChange = { displayName = it },
                    label = { Text("Display Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentMagenta,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Bio
                OutlinedTextField(
                    value = bio,
                    onValueChange = { if (it.length <= 200) bio = it },
                    label = { Text("Bio") },
                    placeholder = { Text("Tell us about yourself...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    supportingText = { Text("${bio.length}/200") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentMagenta,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Gender Selection
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showGenderDialog = true },
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color.Transparent
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(TextSecondary)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Gender", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(
                                when (selectedGender) {
                                    Gender.MALE -> "Male"
                                    Gender.FEMALE -> "Female"
                                    else -> "Not specified"
                                },
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Icon(Icons.Default.ArrowDropDown, "Select Gender", tint = TextSecondary)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Birthday Picker
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showBirthdayPicker = true },
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = Color.Transparent
                    ),
                    border = CardDefaults.outlinedCardBorder().copy(
                        brush = androidx.compose.ui.graphics.SolidColor(TextSecondary)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Birthday", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            Text(
                                birthday?.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
                                    ?: "Not set",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Icon(Icons.Default.CalendarToday, "Select Birthday", tint = TextSecondary)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
    
    // Gender Selection Dialog
    if (showGenderDialog) {
        AlertDialog(
            onDismissRequest = { showGenderDialog = false },
            title = { Text("Select Gender") },
            text = {
                Column {
                    listOf(
                        Gender.MALE to "Male",
                        Gender.FEMALE to "Female",
                        Gender.UNSPECIFIED to "Prefer not to say"
                    ).forEach { (gender, label) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedGender = gender
                                    showGenderDialog = false
                                }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedGender == gender,
                                onClick = {
                                    selectedGender = gender
                                    showGenderDialog = false
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showGenderDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
    
    // Image Source Dialog (Camera or Gallery)
    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Choose Image Source") },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            if (isEditingAvatar) {
                                avatarLauncher.launch("image/*")
                            } else {
                                coverLauncher.launch("image/*")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.PhotoLibrary, "Gallery")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Choose from Gallery")
                    }
                    
                    TextButton(
                        onClick = {
                            showImageSourceDialog = false
                            // Camera functionality would go here
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.CameraAlt, "Camera")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Take Photo")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showImageSourceDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
