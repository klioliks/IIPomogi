package ru.iipomogi.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val CosmicColorScheme = darkColorScheme(
    primary = AppColors.AccentPurple,
    onPrimary = AppColors.TextPrimary,
    secondary = AppColors.AccentCyan,
    onSecondary = AppColors.CosmicDeep,
    tertiary = AppColors.AccentLavender,
    background = AppColors.CosmicDeep,
    onBackground = AppColors.TextPrimary,
    surface = AppColors.CosmicIndigo,
    onSurface = AppColors.TextPrimary,
    surfaceVariant = AppColors.CosmicViolet,
    onSurfaceVariant = AppColors.TextSecondary,
    outline = AppColors.CardBorder,
    error = AppColors.Error
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? android.app.Activity)?.window ?: return@SideEffect
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = CosmicColorScheme,
        typography = Typography,
        shapes = AppMaterialShapes,
        content = content
    )
}

/** Совместимость со старым именем темы из шаблона Android Studio. */
@Composable
fun IIPomogiTheme(content: @Composable () -> Unit) = AppTheme(content = content)
