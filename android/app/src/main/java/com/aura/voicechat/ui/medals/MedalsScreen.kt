package com.aura.voicechat.ui.medals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.ui.theme.*

/**
 * Medal System Screen
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Medal Categories:
 * - Gift Medals (sending/receiving)
 * - Achievement Medals (level milestones)
 * - Activity Medals (login streaks)
 * - Special Medals (CP, Family, Events)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedalsScreen(
    onNavigateBack: () -> Unit,
    viewModel: MedalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedCategory by remember { mutableStateOf("all") }
    
    val categories = listOf("all" to "All", "gift" to "Gift", "achievement" to "Achievement", "activity" to "Activity", "special" to "Special")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Medals") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
        ) {
            // Medal Summary Header
            MedalSummaryHeader(
                totalMedals = uiState.totalMedals,
                earnedMedals = uiState.earnedMedals,
                displayedMedals = uiState.displayedMedals
            )
            
            // Category Filter
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { (id, name) ->
                    FilterChip(
                        selected = selectedCategory == id,
                        onClick = { selectedCategory = id },
                        label = { Text(name) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = AccentMagenta,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Medals Grid
            val filteredMedals = if (selectedCategory == "all") {
                uiState.medals
            } else {
                uiState.medals.filter { it.category == selectedCategory }
            }
            
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredMedals.chunked(2)) { rowMedals ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowMedals.forEach { medal ->
                            MedalCard(
                                medal = medal,
                                modifier = Modifier.weight(1f),
                                onToggleDisplay = { viewModel.toggleMedalDisplay(medal.id) }
                            )
                        }
                        // Fill empty space if odd number
                        if (rowMedals.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MedalSummaryHeader(
    totalMedals: Int,
    earnedMedals: Int,
    displayedMedals: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$earnedMedals",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = VipGold
                    )
                    Text(
                        text = "Earned",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = DarkSurface
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$totalMedals",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
                
                Divider(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp),
                    color = DarkSurface
                )
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${displayedMedals.size}/10",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = AccentMagenta
                    )
                    Text(
                        text = "Displayed",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Displayed medals preview
            if (displayedMedals.isNotEmpty()) {
                Text(
                    text = "Your Profile Medals",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    displayedMedals.take(5).forEach { medalId ->
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(VipGold.copy(alpha = 0.2f))
                                .border(2.dp, VipGold, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Stars,
                                contentDescription = null,
                                tint = VipGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    if (displayedMedals.size > 5) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(DarkSurface),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${displayedMedals.size - 5}",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MedalCard(
    medal: Medal,
    modifier: Modifier = Modifier,
    onToggleDisplay: () -> Unit
) {
    val isEarned = medal.progress >= medal.target
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isEarned) DarkCard else DarkSurface
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Medal Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(
                        if (isEarned) {
                            when (medal.category) {
                                "gift" -> AccentMagenta.copy(alpha = 0.2f)
                                "achievement" -> VipGold.copy(alpha = 0.2f)
                                "activity" -> SuccessGreen.copy(alpha = 0.2f)
                                else -> DiamondBlue.copy(alpha = 0.2f)
                            }
                        } else {
                            DarkCanvas
                        }
                    )
                    .then(
                        if (isEarned && medal.isDisplayed) {
                            Modifier.border(
                                2.dp,
                                when (medal.category) {
                                    "gift" -> AccentMagenta
                                    "achievement" -> VipGold
                                    "activity" -> SuccessGreen
                                    else -> DiamondBlue
                                },
                                CircleShape
                            )
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (medal.category) {
                        "gift" -> Icons.Default.CardGiftcard
                        "achievement" -> Icons.Default.EmojiEvents
                        "activity" -> Icons.Default.DateRange
                        else -> Icons.Default.Favorite
                    },
                    contentDescription = null,
                    tint = if (isEarned) {
                        when (medal.category) {
                            "gift" -> AccentMagenta
                            "achievement" -> VipGold
                            "activity" -> SuccessGreen
                            else -> DiamondBlue
                        }
                    } else TextTertiary,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = medal.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (isEarned) TextPrimary else TextTertiary,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            
            Text(
                text = medal.description,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Progress
            if (!isEarned) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(
                        progress = { (medal.progress.toFloat() / medal.target).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = when (medal.category) {
                            "gift" -> AccentMagenta
                            "achievement" -> VipGold
                            "activity" -> SuccessGreen
                            else -> DiamondBlue
                        },
                        trackColor = DarkCanvas
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${formatNumber(medal.progress)}/${formatNumber(medal.target)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Display toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = medal.isDisplayed,
                        onCheckedChange = { onToggleDisplay() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = AccentMagenta,
                            uncheckedColor = TextTertiary
                        )
                    )
                    Text(
                        text = "Display",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

// Data class
data class Medal(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    val progress: Long,
    val target: Long,
    val isDisplayed: Boolean = false
)

private fun formatNumber(number: Long): String {
    return when {
        number >= 1_000_000 -> String.format("%.1fM", number / 1_000_000.0)
        number >= 1_000 -> String.format("%.1fK", number / 1_000.0)
        else -> number.toString()
    }
}
