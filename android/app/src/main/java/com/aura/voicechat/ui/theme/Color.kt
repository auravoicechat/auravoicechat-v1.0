package com.aura.voicechat.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Aura Voice Chat Color Palette
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Theme: Purple (#c9a8f1) → White gradient
 * Dark Canvas: #12141a
 * Accent Magenta: #d958ff
 * Accent Cyan: #35e8ff
 */

// Primary Purple Palette
val Purple80 = Color(0xFFC9A8F1)
val Purple60 = Color(0xFFA77BDB)
val Purple40 = Color(0xFF8551C5)
val Purple20 = Color(0xFF6329AF)

// Accent Colors
val AccentMagenta = Color(0xFFD958FF)
val AccentCyan = Color(0xFF35E8FF)
val AccentGold = Color(0xFFFFD700)

// Dark Theme
val DarkCanvas = Color(0xFF12141A)
val DarkSurface = Color(0xFF1A1C24)
val DarkCard = Color(0xFF22242E)

// Light Theme
val LightBackground = Color(0xFFFFFBFE)
val LightSurface = Color(0xFFF8F5FF)

// Gradient Colors
val GradientPurpleStart = Color(0xFFC9A8F1)
val GradientPurpleEnd = Color(0xFFFFFFFF)

// VIP Colors
val VipGold = Color(0xFFFFD700)
val VipPlatinum = Color(0xFFE5E4E2)
val VipDiamond = Color(0xFFB9F2FF)

// Status Colors
val SuccessGreen = Color(0xFF4CAF50)
val WarningOrange = Color(0xFFFF9800)
val ErrorRed = Color(0xFFF44336)
val InfoBlue = Color(0xFF2196F3)

// Text Colors
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xB3FFFFFF) // 70% opacity
val TextTertiary = Color(0x80FFFFFF) // 50% opacity
val TextOnLight = Color(0xFF1A1C24)

// Coin Colors
val CoinGold = Color(0xFFFFD700)
val DiamondBlue = Color(0xFF00BFFF)

// Gradient Brushes
// Aura Purple Gradient
val AuraGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFC9A8F1), Color(0xFFD958FF))
)

// Aura Glow Gradient (for buttons, highlights)
val AuraGlowGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFD958FF), Color(0xFF35E8FF))
)
