package com.aura.voicechat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aura.voicechat.ui.auth.LoginScreen
import com.aura.voicechat.ui.auth.OtpVerificationScreen
import com.aura.voicechat.ui.auth.PhoneLoginScreen
import com.aura.voicechat.ui.auth.RegisterScreen
import com.aura.voicechat.ui.auth.SplashScreen
import com.aura.voicechat.ui.cp.CpScreen
import com.aura.voicechat.ui.dailyreward.DailyRewardScreen
import com.aura.voicechat.ui.events.EventsScreen
import com.aura.voicechat.ui.family.FamilyScreen
import com.aura.voicechat.ui.followers.FollowListScreen
import com.aura.voicechat.ui.friends.FriendsScreen
import com.aura.voicechat.ui.games.GamesScreen
import com.aura.voicechat.ui.games.GiftWheelScreen
import com.aura.voicechat.ui.games.Lucky777Screen
import com.aura.voicechat.ui.games.LuckyFruitScreen
import com.aura.voicechat.ui.home.HomeScreen
import com.aura.voicechat.ui.inventory.InventoryScreen
import com.aura.voicechat.ui.kyc.KycScreen
import com.aura.voicechat.ui.level.LevelScreen
import com.aura.voicechat.ui.medals.MedalsScreen
import com.aura.voicechat.ui.messages.MessagesScreen
import com.aura.voicechat.ui.profile.ProfileScreen
import com.aura.voicechat.ui.ranking.RankingScreen
import com.aura.voicechat.ui.referral.ReferralScreen
import com.aura.voicechat.ui.room.RoomScreen
import com.aura.voicechat.ui.search.SearchScreen
import com.aura.voicechat.ui.settings.SettingsScreen
import com.aura.voicechat.ui.store.StoreScreen
import com.aura.voicechat.ui.vip.VipScreen
import com.aura.voicechat.ui.wallet.WalletScreen

/**
 * Navigation Routes for Aura Voice Chat
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Complete navigation system with all app features
 */
sealed class Screen(val route: String) {
    // Auth
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Register : Screen("register")
    object PhoneLogin : Screen("phone_login")
    object OtpVerification : Screen("otp_verification/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp_verification/$phoneNumber"
    }
    
    // Main
    object Home : Screen("home")
    object Messages : Screen("messages")
    object Me : Screen("me")
    
    // Room
    object Room : Screen("room/{roomId}") {
        fun createRoute(roomId: String) = "room/$roomId"
    }
    
    // Profile
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
    object Followers : Screen("followers/{userId}") {
        fun createRoute(userId: String) = "followers/$userId"
    }
    object Following : Screen("following/{userId}") {
        fun createRoute(userId: String) = "following/$userId"
    }
    
    // Wallet & Store
    object Wallet : Screen("wallet")
    object Store : Screen("store")
    object StoreItem : Screen("store/item/{itemId}") {
        fun createRoute(itemId: String) = "store/item/$itemId"
    }
    object Inventory : Screen("inventory")
    
    // Rewards & Progression
    object DailyRewards : Screen("daily_rewards")
    object Level : Screen("level")
    object Medals : Screen("medals")
    object Vip : Screen("vip")
    
    // Social
    object Friends : Screen("friends")
    object CpPartner : Screen("cp_partner")
    object Family : Screen("family")
    object FamilyDetail : Screen("family/{familyId}") {
        fun createRoute(familyId: String) = "family/$familyId"
    }
    
    // Games
    object Games : Screen("games")
    object GiftWheel : Screen("games/gift_wheel")
    object Lucky777 : Screen("games/lucky_777")
    object LuckyFruit : Screen("games/lucky_fruit")
    object GreedyBaby : Screen("games/greedy_baby")
    
    // Rankings
    object RankingSender : Screen("ranking/sender")
    object RankingReceiver : Screen("ranking/receiver")
    object RankingFamily : Screen("ranking/family")
    object WeeklyPartyStar : Screen("ranking/party_star")
    
    // Events
    object RechargeEvent : Screen("events/recharge")
    object RoomSupport : Screen("events/room_support/{roomId}") {
        fun createRoute(roomId: String) = "events/room_support/$roomId"
    }
    object EventDetail : Screen("events/{eventId}") {
        fun createRoute(eventId: String) = "events/$eventId"
    }
    object LuckyDraw : Screen("events/lucky_draw")
    
    // Cinema
    object Cinema : Screen("room/{roomId}/cinema") {
        fun createRoute(roomId: String) = "room/$roomId/cinema"
    }
    
    // Settings - Extended
    object HelpCenter : Screen("settings/help")
    object Feedback : Screen("settings/feedback")
    object UpdateCheck : Screen("settings/update")
    
    // Guide System
    object GuideApplication : Screen("guide/apply")
    
    // Earning System
    object EarningTargets : Screen("earning/targets")
    object EarningTargetsGuide : Screen("earning/targets/guide")
    
    // Owner Panel
    object OwnerPanel : Screen("owner/panel")
    object EconomySetup : Screen("owner/economy")
    
    // Guide Panel
    object GuidePanel : Screen("guide/panel")
    
    // Support System
    object SupportTickets : Screen("support/tickets")
    object LiveChat : Screen("support/chat")
    object LiveChatWithTicket : Screen("support/chat/{ticketId}") {
        fun createRoute(ticketId: String) = "support/chat/$ticketId"
    }
    
    // Utility
    object Search : Screen("search")
    object Referral : Screen("referral")
    object Settings : Screen("settings")
    object Kyc : Screen("kyc")
}

@Composable
fun AuraNavHost(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // ===== AUTH =====
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Login.route) {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToOtp = { phone ->
                    navController.navigate(Screen.OtpVerification.createRoute(phone))
                }
            )
        }
        
        composable(Screen.Register.route) {
            RegisterScreen(
                onBack = { navController.popBackStack() },
                onRegisterComplete = { username, displayName, dob, gender ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.PhoneLogin.route) {
            PhoneLoginScreen(
                onBack = { navController.popBackStack() },
                onSendOtp = { countryCode, phoneNumber ->
                    val fullNumber = "$countryCode$phoneNumber"
                    navController.navigate(Screen.OtpVerification.createRoute(fullNumber))
                }
            )
        }
        
        composable(
            route = Screen.OtpVerification.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber") ?: ""
            OtpVerificationScreen(
                phoneNumber = phoneNumber,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== MAIN SCREENS =====
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToRoom = { roomId -> navController.navigate(Screen.Room.createRoute(roomId)) },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) },
                onNavigateToWallet = { navController.navigate(Screen.Wallet.route) },
                onNavigateToDailyRewards = { navController.navigate(Screen.DailyRewards.route) },
                onNavigateToKyc = { navController.navigate(Screen.Kyc.route) },
                onNavigateToStore = { navController.navigate(Screen.Store.route) },
                onNavigateToInventory = { navController.navigate(Screen.Inventory.route) },
                onNavigateToSearch = { navController.navigate(Screen.Search.route) },
                onNavigateToRanking = { navController.navigate(Screen.RankingSender.route) },
                onNavigateToGames = { navController.navigate(Screen.Games.route) }
            )
        }
        
        composable(Screen.Messages.route) {
            MessagesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToChat = { chatId -> /* Navigate to chat */ },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) }
            )
        }
        
        // ===== ROOM =====
        composable(
            route = Screen.Room.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            RoomScreen(
                roomId = roomId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== PROFILE & SOCIAL =====
        composable(
            route = Screen.Profile.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.Followers.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            FollowListScreen(
                type = "followers",
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate(Screen.Profile.createRoute(it)) }
            )
        }
        
        composable(
            route = Screen.Following.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            FollowListScreen(
                type = "following",
                userId = userId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { navController.navigate(Screen.Profile.createRoute(it)) }
            )
        }
        
        composable(Screen.Friends.route) {
            FriendsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) }
            )
        }
        
        composable(Screen.CpPartner.route) {
            CpScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPartnerProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) }
            )
        }
        
        composable(Screen.Family.route) {
            FamilyScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToMemberProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) },
                onNavigateToFamilyRanking = { navController.navigate(Screen.RankingFamily.route) }
            )
        }
        
        // ===== WALLET & STORE =====
        composable(Screen.Wallet.route) {
            WalletScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Store.route) {
            StoreScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToItem = { itemId -> navController.navigate(Screen.StoreItem.createRoute(itemId)) }
            )
        }
        
        composable(Screen.Inventory.route) {
            InventoryScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToStore = { navController.navigate(Screen.Store.route) }
            )
        }
        
        // ===== REWARDS & PROGRESSION =====
        composable(Screen.DailyRewards.route) {
            DailyRewardScreen(
                onNavigateBack = { navController.popBackStack() },
                onClaimReward = { /* Handle claim */ }
            )
        }
        
        composable(Screen.Level.route) {
            LevelScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Medals.route) {
            MedalsScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Vip.route) {
            VipScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPurchase = { /* Navigate to purchase */ }
            )
        }
        
        // ===== GAMES =====
        composable(Screen.Games.route) {
            GamesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToGame = { gameId ->
                    when (gameId) {
                        "gift_wheel" -> navController.navigate(Screen.GiftWheel.route)
                        "lucky_777" -> navController.navigate(Screen.Lucky777.route)
                        "lucky_fruit" -> navController.navigate(Screen.LuckyFruit.route)
                        "greedy_baby" -> navController.navigate(Screen.GreedyBaby.route)
                    }
                }
            )
        }
        
        composable(Screen.GiftWheel.route) {
            GiftWheelScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Lucky777.route) {
            Lucky777Screen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.LuckyFruit.route) {
            LuckyFruitScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        // ===== RANKINGS =====
        composable(Screen.RankingSender.route) {
            RankingScreen(
                rankingType = "sender",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) },
                onNavigateToFamily = { familyId -> navController.navigate(Screen.FamilyDetail.createRoute(familyId)) }
            )
        }
        
        composable(Screen.RankingReceiver.route) {
            RankingScreen(
                rankingType = "receiver",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) },
                onNavigateToFamily = { familyId -> navController.navigate(Screen.FamilyDetail.createRoute(familyId)) }
            )
        }
        
        composable(Screen.RankingFamily.route) {
            RankingScreen(
                rankingType = "family",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) },
                onNavigateToFamily = { familyId -> navController.navigate(Screen.FamilyDetail.createRoute(familyId)) }
            )
        }
        
        composable(Screen.WeeklyPartyStar.route) {
            EventsScreen(
                eventType = "party_star",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) }
            )
        }
        
        // ===== EVENTS =====
        composable(Screen.RechargeEvent.route) {
            EventsScreen(
                eventType = "recharge",
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) }
            )
        }
        
        // ===== UTILITY =====
        composable(Screen.Search.route) {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToProfile = { userId -> navController.navigate(Screen.Profile.createRoute(userId)) },
                onNavigateToRoom = { roomId -> navController.navigate(Screen.Room.createRoute(roomId)) }
            )
        }
        
        composable(Screen.Referral.route) {
            ReferralScreen(onNavigateBack = { navController.popBackStack() })
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToPrivacy = { /* Navigate to privacy */ },
                onNavigateToBlocked = { /* Navigate to blocked users */ },
                onNavigateToAbout = { /* Navigate to about */ },
                onNavigateToHelp = { navController.navigate(Screen.HelpCenter.route) },
                onNavigateToFeedback = { navController.navigate(Screen.Feedback.route) },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Kyc.route) {
            KycScreen(
                onNavigateBack = { navController.popBackStack() },
                onKycComplete = { navController.popBackStack() }
            )
        }
        
        // ===== CINEMA =====
        composable(
            route = Screen.Cinema.route,
            arguments = listOf(navArgument("roomId") { type = NavType.StringType })
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            com.aura.voicechat.ui.room.cinema.CinemaScreen(
                roomId = roomId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== EVENT DETAILS =====
        composable(
            route = Screen.EventDetail.route,
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId") ?: ""
            com.aura.voicechat.ui.events.EventDetailScreen(
                eventId = eventId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== LUCKY DRAW =====
        composable(Screen.LuckyDraw.route) {
            com.aura.voicechat.ui.events.LuckyDrawScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== SETTINGS - EXTENDED =====
        composable(Screen.HelpCenter.route) {
            com.aura.voicechat.ui.settings.HelpCenterScreen(
                onNavigateBack = { navController.popBackStack() },
                onContactSupport = { navController.navigate(Screen.Feedback.route) }
            )
        }
        
        composable(Screen.Feedback.route) {
            com.aura.voicechat.ui.settings.FeedbackScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.UpdateCheck.route) {
            com.aura.voicechat.ui.settings.UpdateCheckScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== GUIDE SYSTEM =====
        composable(Screen.GuideApplication.route) {
            com.aura.voicechat.ui.guide.GuideApplicationScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== EARNING SYSTEM =====
        composable(Screen.EarningTargets.route) {
            com.aura.voicechat.ui.earning.EarningTargetSheetScreen(
                isGuide = false,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.EarningTargetsGuide.route) {
            com.aura.voicechat.ui.earning.EarningTargetSheetScreen(
                isGuide = true,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== OWNER PANEL =====
        composable(Screen.OwnerPanel.route) {
            com.aura.voicechat.ui.owner.OwnerPanelScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEconomy = { navController.navigate(Screen.EconomySetup.route) },
                onNavigateToAdmins = { /* TODO: Admin management */ },
                onNavigateToCashouts = { /* TODO: Cashout approvals */ },
                onNavigateToGuides = { /* TODO: Guide management */ },
                onNavigateToAnalytics = { /* TODO: Analytics */ }
            )
        }
        
        composable(Screen.EconomySetup.route) {
            com.aura.voicechat.ui.owner.EconomySetupScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        // ===== GUIDE PANEL =====
        composable(Screen.GuidePanel.route) {
            com.aura.voicechat.ui.guide.GuidePanelScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToEarnings = { navController.navigate(Screen.EarningTargetsGuide.route) },
                onNavigateToSessions = { /* TODO: Session history */ },
                onNavigateToTargets = { navController.navigate(Screen.EarningTargetsGuide.route) }
            )
        }
        
        // ===== SUPPORT SYSTEM =====
        composable(Screen.SupportTickets.route) {
            com.aura.voicechat.ui.support.SupportTicketsScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToTicket = { ticketId -> 
                    navController.navigate(Screen.LiveChatWithTicket.createRoute(ticketId))
                },
                onNavigateToNewTicket = { /* TODO: New ticket form */ },
                onNavigateToLiveChat = { navController.navigate(Screen.LiveChat.route) }
            )
        }
        
        composable(Screen.LiveChat.route) {
            com.aura.voicechat.ui.support.LiveChatScreen(
                ticketId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.LiveChatWithTicket.route,
            arguments = listOf(navArgument("ticketId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ticketId = backStackEntry.arguments?.getString("ticketId")
            com.aura.voicechat.ui.support.LiveChatScreen(
                ticketId = ticketId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
