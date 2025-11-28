package com.aura.voicechat.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.aura.voicechat.domain.model.RoomCard
import com.aura.voicechat.domain.model.RoomType
import com.aura.voicechat.ui.theme.*

/**
 * Home Screen with Mine/Popular tabs, room cards, banner carousel, Rewards FAB
 * Developer: Hawkaye Visions LTD — Pakistan
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRoom: (String) -> Unit,
    onNavigateToProfile: (String) -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToDailyRewards: () -> Unit,
    onNavigateToKyc: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Mine", "Popular")
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Aura Voice Chat",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { /* Messages */ }) {
                        Icon(Icons.Default.Email, contentDescription = "Messages")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCanvas
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToDailyRewards,
                containerColor = AccentMagenta,
                contentColor = Color.White,
                icon = { Icon(Icons.Default.CardGiftcard, contentDescription = null) },
                text = { Text("Rewards") }
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Explore */ },
                    icon = { Icon(Icons.Default.Explore, contentDescription = null) },
                    label = { Text("Explore") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Create Room */ },
                    icon = { 
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(AccentMagenta, Purple80)
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    },
                    label = { Text("Create") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToWallet,
                    icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) },
                    label = { Text("Wallet") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigateToProfile("me") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("Me") }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkCanvas),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Banner Carousel
            item {
                BannerCarousel(
                    banners = uiState.banners,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(16.dp)
                )
            }
            
            // Quick Actions
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        QuickActionChip(
                            icon = Icons.Default.Star,
                            text = "VIP",
                            onClick = { }
                        )
                    }
                    item {
                        QuickActionChip(
                            icon = Icons.Default.Favorite,
                            text = "CP",
                            onClick = { }
                        )
                    }
                    item {
                        QuickActionChip(
                            icon = Icons.Default.Badge,
                            text = "Medals",
                            onClick = { }
                        )
                    }
                    item {
                        QuickActionChip(
                            icon = Icons.Default.VerifiedUser,
                            text = "KYC",
                            onClick = onNavigateToKyc
                        )
                    }
                }
            }
            
            // Tab Row
            item {
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = Purple80,
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }
            }
            
            // Room Cards
            val rooms = if (selectedTab == 0) uiState.myRooms else uiState.popularRooms
            items(rooms) { room ->
                RoomCardItem(
                    room = room,
                    onClick = { onNavigateToRoom(room.id) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
            
            // Empty state
            if (rooms.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.MicOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = TextTertiary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = if (selectedTab == 0) "No rooms yet" else "No popular rooms",
                                color = TextTertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BannerCarousel(
    banners: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { maxOf(banners.size, 1) })
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Box {
            HorizontalPager(state = pagerState) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Purple40, AccentMagenta)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Welcome to Aura Voice Chat!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
            
            // Indicators
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(maxOf(banners.size, 1)) { index ->
                    Box(
                        modifier = Modifier
                            .size(if (pagerState.currentPage == index) 8.dp else 6.dp)
                            .background(
                                color = if (pagerState.currentPage == index) Color.White else Color.White.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickActionChip(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        label = { Text(text) },
        selected = false,
        leadingIcon = {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = DarkCard,
            labelColor = TextPrimary
        )
    )
}

@Composable
private fun RoomCardItem(
    room: RoomCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCard)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Room Cover
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Purple40.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                if (room.coverImage != null) {
                    AsyncImage(
                        model = room.coverImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        when (room.type) {
                            RoomType.VOICE -> Icons.Default.Mic
                            RoomType.VIDEO -> Icons.Default.Videocam
                            RoomType.MUSIC -> Icons.Default.MusicNote
                        },
                        contentDescription = null,
                        tint = Purple80,
                        modifier = Modifier.size(36.dp)
                    )
                }
                
                // Live indicator
                if (room.isLive) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(ErrorRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "LIVE",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = room.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Owner avatar
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Purple40)
                    )
                    
                    Spacer(modifier = Modifier.width(6.dp))
                    
                    Text(
                        text = room.ownerName,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Tags
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(room.tags.take(3)) { tag ->
                        Box(
                            modifier = Modifier
                                .background(Purple40.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = tag,
                                style = MaterialTheme.typography.labelSmall,
                                color = Purple80
                            )
                        }
                    }
                }
            }
            
            // User count
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.People,
                    contentDescription = null,
                    tint = AccentCyan,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "${room.userCount}/${room.capacity}",
                    style = MaterialTheme.typography.labelMedium,
                    color = AccentCyan
                )
            }
        }
    }
}
