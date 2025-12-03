package com.aura.voicechat.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.*

/**
 * Lottie Player Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Composable wrapper for Lottie animation player
 */
@Composable
fun LottiePlayer(
    assetName: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f,
    onAnimationEnd: () -> Unit = {}
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/$assetName")
    )
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        restartOnPlay = restartOnPlay,
        iterations = iterations,
        speed = speed
    )
    
    // Detect when animation completes
    LaunchedEffect(progress) {
        if (progress == 1f && iterations != LottieConstants.IterateForever) {
            onAnimationEnd()
        }
    }
    
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

/**
 * Lottie Player from URL
 */
@Composable
fun LottiePlayerFromUrl(
    url: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f,
    onAnimationEnd: () -> Unit = {}
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Url(url)
    )
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        restartOnPlay = restartOnPlay,
        iterations = iterations,
        speed = speed
    )
    
    LaunchedEffect(progress) {
        if (progress == 1f && iterations != LottieConstants.IterateForever) {
            onAnimationEnd()
        }
    }
    
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

/**
 * Lottie Player from raw JSON string
 */
@Composable
fun LottiePlayerFromJson(
    jsonString: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    iterations: Int = LottieConstants.IterateForever,
    speed: Float = 1f,
    onAnimationEnd: () -> Unit = {}
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.JsonString(jsonString)
    )
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        restartOnPlay = restartOnPlay,
        iterations = iterations,
        speed = speed
    )
    
    LaunchedEffect(progress) {
        if (progress == 1f && iterations != LottieConstants.IterateForever) {
            onAnimationEnd()
        }
    }
    
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

/**
 * One-shot Lottie animation
 */
@Composable
fun LottieOneShot(
    assetName: String,
    modifier: Modifier = Modifier,
    speed: Float = 1f,
    onComplete: () -> Unit = {}
) {
    LottiePlayer(
        assetName = assetName,
        modifier = modifier,
        isPlaying = true,
        restartOnPlay = false,
        iterations = 1,
        speed = speed,
        onAnimationEnd = onComplete
    )
}

/**
 * Looping Lottie animation
 */
@Composable
fun LottieLoop(
    assetName: String,
    modifier: Modifier = Modifier,
    speed: Float = 1f
) {
    LottiePlayer(
        assetName = assetName,
        modifier = modifier,
        isPlaying = true,
        restartOnPlay = true,
        iterations = LottieConstants.IterateForever,
        speed = speed
    )
}

/**
 * Lottie Player with manual control
 */
@Composable
fun LottiePlayerWithControl(
    assetName: String,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = true,
    speed: Float = 1f,
    onAnimationEnd: () -> Unit = {}
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("lottie/$assetName")
    )
    
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying,
        speed = speed,
        iterations = LottieConstants.IterateForever
    )
    
    LaunchedEffect(progress) {
        if (progress == 1f) {
            onAnimationEnd()
        }
    }
    
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier
    )
}

/**
 * Full-screen Lottie animation overlay
 */
@Composable
fun LottieFullScreenAnimation(
    assetName: String,
    onComplete: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LottiePlayer(
            assetName = assetName,
            modifier = Modifier.fillMaxSize(),
            isPlaying = true,
            restartOnPlay = false,
            iterations = 1,
            onAnimationEnd = onComplete
        )
    }
}

/**
 * Gift animation with Lottie
 */
@Composable
fun GiftLottieAnimation(
    giftAssetName: String,
    modifier: Modifier = Modifier,
    onComplete: () -> Unit = {}
) {
    LottiePlayer(
        assetName = giftAssetName,
        modifier = modifier,
        isPlaying = true,
        restartOnPlay = false,
        iterations = 1,
        speed = 1.2f, // Slightly faster for gifts
        onAnimationEnd = onComplete
    )
}

/**
 * Loading animation with Lottie
 */
@Composable
fun LottieLoadingAnimation(
    modifier: Modifier = Modifier,
    assetName: String = "loading.json"
) {
    LottieLoop(
        assetName = assetName,
        modifier = modifier,
        speed = 1.5f
    )
}
