package com.aura.voicechat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aura.voicechat.ui.theme.AuraGradient
import com.aura.voicechat.ui.theme.TextPrimary

/**
 * Aura Button Component
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Primary button with purple gradient background
 */

@Composable
fun AuraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    gradient: Brush = AuraGradient,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(
                brush = gradient,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.matchParentSize(),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = TextPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = TextPrimary.copy(alpha = 0.5f)
            ),
            shape = RoundedCornerShape(12.dp),
            contentPadding = contentPadding
        ) {
            Text(text = text)
        }
    }
}
