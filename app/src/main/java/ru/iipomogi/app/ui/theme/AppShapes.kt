package ru.iipomogi.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

object AppShapes {
    val ExtraSmall = RoundedCornerShape(8.dp)
    val Small = RoundedCornerShape(12.dp)
    val Medium = RoundedCornerShape(20.dp)
    val Large = RoundedCornerShape(24.dp)
    val ExtraLarge = RoundedCornerShape(28.dp)
    val Card = RoundedCornerShape(24.dp)
    val Button = RoundedCornerShape(50)
    val Badge = RoundedCornerShape(50)
    val Dialog = RoundedCornerShape(28.dp)
}

val AppMaterialShapes = Shapes(
    extraSmall = AppShapes.ExtraSmall,
    small = AppShapes.Small,
    medium = AppShapes.Medium,
    large = AppShapes.Large,
    extraLarge = AppShapes.ExtraLarge
)
