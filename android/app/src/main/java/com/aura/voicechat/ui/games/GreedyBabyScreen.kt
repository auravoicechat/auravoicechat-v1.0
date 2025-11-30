package com.aura.voicechat.ui.games

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.aura.voicechat.R

/**
 * Greedy Baby Game Screen
 * A feeding game where users feed a baby to win rewards
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GreedyBabyScreen(
    onNavigateBack: () -> Unit,
    viewModel: GreedyBabyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Greedy Baby") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1A1A2E),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                actions = {
                    // Coins balance
                    Row(
                        modifier = Modifier
                            .background(Color(0xFF2D2D44), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFFFD700),
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "%,d".format(uiState.userCoins),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF1A1A2E), Color(0xFF16213E))
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Game Info Card
                GameInfoCard(
                    feedCount = uiState.feedCount,
                    maxFeeds = uiState.maxFeeds,
                    happinessLevel = uiState.happinessLevel
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Baby Display
                BabyDisplay(
                    isHappy = uiState.isHappy,
                    isFull = uiState.isFull,
                    isFeeding = uiState.isFeeding,
                    happinessLevel = uiState.happinessLevel
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Food Selection Grid
                Text(
                    text = "Select Food to Feed Baby",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                FoodSelectionGrid(
                    foods = uiState.availableFoods,
                    selectedFood = uiState.selectedFood,
                    onFoodSelected = { viewModel.selectFood(it) }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Feed Button
                FeedButton(
                    enabled = uiState.canFeed,
                    isFeeding = uiState.isFeeding,
                    cost = uiState.selectedFood?.cost ?: 0,
                    onClick = { viewModel.feedBaby() }
                )

                // Result Dialog
                if (uiState.showResult) {
                    ResultDialog(
                        reward = uiState.reward,
                        onDismiss = { viewModel.dismissResult() }
                    )
                }
            }
        }
    }
}

@Composable
private fun GameInfoCard(
    feedCount: Int,
    maxFeeds: Int,
    happinessLevel: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D2D44)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            InfoItem(
                icon = Icons.Default.Restaurant,
                label = "Feeds Today",
                value = "$feedCount/$maxFeeds"
            )
            InfoItem(
                icon = Icons.Default.Favorite,
                label = "Happiness",
                value = "$happinessLevel%"
            )
            InfoItem(
                icon = Icons.Default.Star,
                label = "Level",
                value = "${happinessLevel / 20 + 1}"
            )
        }
    }
}

@Composable
private fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            tint = Color(0xFFFFD700),
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 12.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun BabyDisplay(
    isHappy: Boolean,
    isFull: Boolean,
    isFeeding: Boolean,
    happinessLevel: Int
) {
    val scale by animateFloatAsState(
        targetValue = if (isFeeding) 1.1f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(200.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFE4B5),
                        Color(0xFFFFD39B)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Baby Face Emoji based on state
            Text(
                text = when {
                    isFull -> "😋"
                    isHappy -> "😊"
                    happinessLevel > 50 -> "🙂"
                    happinessLevel > 25 -> "😐"
                    else -> "😢"
                },
                fontSize = 80.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Status Text
            Text(
                text = when {
                    isFull -> "I'm Full!"
                    isHappy -> "Yummy!"
                    happinessLevel > 50 -> "Feed Me!"
                    happinessLevel > 25 -> "Hungry..."
                    else -> "Very Hungry!"
                },
                color = Color(0xFF5D4037),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun FoodSelectionGrid(
    foods: List<FoodItem>,
    selectedFood: FoodItem?,
    onFoodSelected: (FoodItem) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(200.dp)
    ) {
        items(foods) { food ->
            FoodCard(
                food = food,
                isSelected = food == selectedFood,
                onClick = { onFoodSelected(food) }
            )
        }
    }
}

@Composable
private fun FoodCard(
    food: FoodItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFFFD700) else Color(0xFF3D3D54)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = food.emoji,
                fontSize = 28.sp
            )
            Text(
                text = food.name,
                color = if (isSelected) Color(0xFF1A1A2E) else Color.White,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
            Text(
                text = "${food.cost}",
                color = if (isSelected) Color(0xFF1A1A2E) else Color(0xFFFFD700),
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun FeedButton(
    enabled: Boolean,
    isFeeding: Boolean,
    cost: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isFeeding,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFFD700),
            contentColor = Color(0xFF1A1A2E),
            disabledContainerColor = Color.Gray
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        if (isFeeding) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color(0xFF1A1A2E)
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(Icons.Default.Restaurant, contentDescription = null)
                Text(
                    text = "Feed Baby ($cost coins)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun ResultDialog(
    reward: GameReward?,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2D2D44),
        title = {
            Text(
                text = if (reward != null) "🎉 Baby is Happy!" else "😢 Baby didn't like it",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (reward != null) {
                    Text(
                        text = "You won:",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "+${reward.amount} ${reward.type}",
                        color = Color(0xFFFFD700),
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                } else {
                    Text(
                        text = "Try again with different food!",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFD700),
                    contentColor = Color(0xFF1A1A2E)
                )
            ) {
                Text("OK")
            }
        }
    )
}

// Data Classes
data class FoodItem(
    val id: String,
    val name: String,
    val emoji: String,
    val cost: Int,
    val happinessBonus: Int,
    val rewardMultiplier: Float
)

data class GameReward(
    val type: String,
    val amount: Int
)
