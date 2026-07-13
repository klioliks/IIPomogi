package ru.iipomogi.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object AppColors {
    val CosmicDeep = Color(0xFF0B0620)
    val CosmicNavy = Color(0xFF151238)
    val CosmicIndigo = Color(0xFF1A1040)
    val CosmicViolet = Color(0xFF2E1A5C)
    val CosmicPurple = Color(0xFF4A2F8C)

    val AccentPurple = Color(0xFF8A70F0)
    val AccentViolet = Color(0xFF6C5CE7)
    val AccentBlue = Color(0xFF5B8DEF)
    val AccentCyan = Color(0xFF5EC8F0)
    val AccentLavender = Color(0xFFC4B5FD)
    val AccentPink = Color(0xFFD4A5F5)

    val TextPrimary = Color(0xFFFFFFFF)
    val TextSecondary = Color(0xFFD0D8F0)
    val TextMuted = Color(0xFFA8B0D0)

    val CardDark = Color(0x33FFFFFF)
    val CardBorder = Color(0x44FFFFFF)
    val GlassLight = Color(0x22FFFFFF)
    val GlassDark = Color(0x280A0618)

    val ButtonPrimaryStart = Color(0xFF8A70F0)
    val ButtonPrimaryEnd = Color(0xFF5B8DEF)
    val ButtonSecondary = Color(0x28FFFFFF)
    val ButtonSecondaryBorder = Color(0x55FFFFFF)

    val Error = Color(0xFFFF8A9A)
    val Success = Color(0xFF7EE8A8)

    val StarGlow = Color(0xCCFFFFFF)
    val OverlayDark = Color(0x99000000)

    val CosmicGradient = Brush.verticalGradient(
        colors = listOf(
            CosmicDeep,
            CosmicIndigo,
            CosmicViolet,
            Color(0xFF1E1448)
        )
    )

    val PrimaryButtonGradient = Brush.horizontalGradient(
        colors = listOf(ButtonPrimaryStart, ButtonPrimaryEnd)
    )

    val SoftGlowGradient = Brush.radialGradient(
        colors = listOf(
            AccentPurple.copy(alpha = 0.35f),
            Color.Transparent
        )
    )
}
