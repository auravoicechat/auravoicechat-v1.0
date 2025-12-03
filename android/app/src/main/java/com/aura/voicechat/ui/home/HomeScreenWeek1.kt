package com.aura.voicechat.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import com.aura.voicechat.domain.model.RoomCategory
import com.aura.voicechat.ui.theme.*

/**
 * HomeScreen with 4 Bottom Tabs - Week 1 Implementation
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Main home screen with bottom navigation:
 * - 4 tabs: Home, Messages, Games, Profile
 * - Bottom navigation bar with purple theme
 * - Uses Material3 NavigationBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToRoom: (roomId: String) -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToGames: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToSearch: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    var selectedCategory by remember { mutableStateOf(RoomCategory.ALL) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Aura Voice Chat",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSearch) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkCanvas,
                    titleContentColor = TextPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = DarkSurface,
                contentColor = AccentMagenta
            ) {
                BottomNavItem.values().forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selectedTab == index,
                        onClick = {
                            selectedTab = index
                            when (index) {
                                0 -> { /* Stay on Home */ }
                                1 -> onNavigateToMessages()
                                2 -> onNavigateToGames()
                                3 -> onNavigateToProfile()
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AccentMagenta,
                            selectedTextColor = AccentMagenta,
                            unselectedIconColor = TextSecondary,
                            unselectedTextColor = TextSecondary,
                            indicatorColor = AccentMagenta.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        },
        containerColor = DarkCanvas
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> {
                    // Home Tab - Show Popular Rooms
                    PopularTab(
                        rooms = uiState.popularRooms,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { category ->
                            selectedCategory = category
                            viewModel.filterByCategory(category)
                        },
                        onRoomClick = onNavigateToRoom,
                        onRefresh = { viewModel.refresh() },
                        isRefreshing = uiState.isLoading
                    )
                }
                1 -> {
                    // Messages Tab - Navigate handled in bottomBar
                }
                2 -> {
                    // Games Tab - Navigate handled in bottomBar
                }
                3 -> {
                    // Profile Tab - Navigate handled in bottomBar
                }
            }
        }
    }
}

/**
 * Bottom Navigation Items
 */
enum class BottomNavItem(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    MESSAGES("Messages", Icons.Default.Message),
    GAMES("Games", Icons.Default.Gamepad),
    PROFILE("Profile", Icons.Default.Person)
}
