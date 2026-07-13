package ru.iipomogi.app.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.iipomogi.app.ui.theme.AppColors
import ru.iipomogi.app.ui.theme.AppTypography

@Composable
fun LoadingState(
    message: String = "Открываю учебный космос…",
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "loading")
    val glowAlpha by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    CosmicBackground(modifier = modifier, showNebulaImage = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(56.dp)
                        .alpha(glowAlpha),
                    color = AppColors.AccentPurple,
                    strokeWidth = 3.dp,
                    trackColor = AppColors.AccentCyan.copy(alpha = 0.2f),
                    strokeCap = StrokeCap.Round
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = message,
                style = AppTypography.Title,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Спроси, проверь, пойми.",
                style = AppTypography.BodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}
