package com.aura.voicechat.ui.family

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil3.compose.AsyncImage
import com.aura.voicechat.ui.theme.*

/**
 * Create Family Dialog
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Dialog for creating a new family
 */
@Composable
fun CreateFamilyDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, bio: String, avatarUri: Uri?) -> Unit
) {
    var familyName by remember { mutableStateOf("") }
    var familyBio by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    
    val avatarLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { avatarUri = it }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = DarkCanvas
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Create Family",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, "Close", tint = TextSecondary)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Avatar Upload
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        AsyncImage(
                            model = avatarUri,
                            contentDescription = "Family Avatar",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Purple40.copy(alpha = 0.2f))
                                .clickable { avatarLauncher.launch("image/*") }
                        )
                        
                        IconButton(
                            onClick = { avatarLauncher.launch("image/*") },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                                .background(AccentMagenta, CircleShape)
                        ) {
                            Icon(
                                Icons.Default.CameraAlt,
                                "Upload Avatar",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = "Tap to upload family avatar",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Family Name Input
                OutlinedTextField(
                    value = familyName,
                    onValueChange = { if (it.length <= 30) familyName = it },
                    label = { Text("Family Name") },
                    placeholder = { Text("Enter family name...") },
                    modifier = Modifier.fillMaxWidth(),
                    supportingText = { Text("${familyName.length}/30") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentMagenta,
                        unfocusedBorderColor = TextSecondary
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Family Bio Input
                OutlinedTextField(
                    value = familyBio,
                    onValueChange = { if (it.length <= 200) familyBio = it },
                    label = { Text("Family Bio") },
                    placeholder = { Text("Tell us about your family...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    maxLines = 5,
                    supportingText = { Text("${familyBio.length}/200") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentMagenta,
                        unfocusedBorderColor = TextSecondary
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AccentMagenta.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Family Benefits:",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = AccentMagenta
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        FamilyBenefitItem("👨‍👩‍👧‍👦 Create a community of up to 100 members")
                        FamilyBenefitItem("🏆 Participate in family rankings")
                        FamilyBenefitItem("💎 Share rewards and bonuses")
                        FamilyBenefitItem("🎉 Host exclusive family events")
                        FamilyBenefitItem("⭐ Unlock special badges and perks")
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Creation Cost Info
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFD700).copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Creation Cost",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "10,000 💎 Diamonds",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFD700)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Create Button
                Button(
                    onClick = {
                        if (familyName.isNotBlank()) {
                            onCreate(familyName, familyBio, avatarUri)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = familyName.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentMagenta,
                        disabledContainerColor = TextSecondary.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Groups,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Create Family",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun FamilyBenefitItem(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color.Green,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}
