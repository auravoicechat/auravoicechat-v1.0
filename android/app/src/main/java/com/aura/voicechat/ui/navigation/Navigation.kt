package com.aura.voicechat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aura.voicechat.ui.auth.LoginScreen
import com.aura.voicechat.ui.auth.OtpVerificationScreen
import com.aura.voicechat.ui.home.HomeScreen
import com.aura.voicechat.ui.kyc.KycScreen
import com.aura.voicechat.ui.profile.ProfileScreen
import com.aura.voicechat.ui.rewards.DailyRewardsScreen
import com.aura.voicechat.ui.room.RoomScreen
import com.aura.voicechat.ui.wallet.WalletScreen

/**
 * Navigation Routes for Aura Voice Chat
 * Developer: Hawkaye Visions LTD — Pakistan
 */
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object OtpVerification : Screen("otp_verification/{phoneNumber}") {
        fun createRoute(phoneNumber: String) = "otp_verification/$phoneNumber"
    }
    object Home : Screen("home")
    object Room : Screen("room/{roomId}") {
        fun createRoute(roomId: String) = "room/$roomId"
    }
    object Profile : Screen("profile/{userId}") {
        fun createRoute(userId: String) = "profile/$userId"
    }
    object Wallet : Screen("wallet")
    object DailyRewards : Screen("daily_rewards")
    object Kyc : Screen("kyc")
    object Store : Screen("store")
    object Medals : Screen("medals")
    object Vip : Screen("vip")
    object CpPartner : Screen("cp_partner")
    object Referral : Screen("referral")
    object Messages : Screen("messages")
    object Settings : Screen("settings")
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
        
        composable(
            route = Screen.OtpVerification.route,
            arguments = listOf(
                navArgument("phoneNumber") { type = NavType.StringType }
            )
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
        
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToRoom = { roomId ->
                    navController.navigate(Screen.Room.createRoute(roomId))
                },
                onNavigateToProfile = { userId ->
                    navController.navigate(Screen.Profile.createRoute(userId))
                },
                onNavigateToWallet = {
                    navController.navigate(Screen.Wallet.route)
                },
                onNavigateToDailyRewards = {
                    navController.navigate(Screen.DailyRewards.route)
                },
                onNavigateToKyc = {
                    navController.navigate(Screen.Kyc.route)
                }
            )
        }
        
        composable(
            route = Screen.Room.route,
            arguments = listOf(
                navArgument("roomId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val roomId = backStackEntry.arguments?.getString("roomId") ?: ""
            RoomScreen(
                roomId = roomId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(
            route = Screen.Profile.route,
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: ""
            ProfileScreen(
                userId = userId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Wallet.route) {
            WalletScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.DailyRewards.route) {
            DailyRewardsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Kyc.route) {
            KycScreen(
                onNavigateBack = { navController.popBackStack() },
                onKycComplete = { navController.popBackStack() }
            )
        }
    }
}
