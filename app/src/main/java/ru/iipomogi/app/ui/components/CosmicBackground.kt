package ru.iipomogi.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import ru.iipomogi.app.R
import ru.iipomogi.app.ui.theme.AppColors
import kotlin.random.Random

@Composable
fun CosmicBackground(
    modifier: Modifier = Modifier,
    showNebulaImage: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (showNebulaImage) {
            Image(
                painter = painterResource(R.drawable.cosmic_nebula),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.85f
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                AppColors.CosmicDeep.copy(alpha = 0.55f),
                                AppColors.CosmicIndigo.copy(alpha = 0.45f),
                                AppColors.CosmicDeep.copy(alpha = 0.75f)
                            )
                        )
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(AppColors.CosmicGradient)
            )
        }

        CosmicStars(modifier = Modifier.fillMaxSize())

        content()
    }
}

@Composable
private fun CosmicStars(modifier: Modifier = Modifier) {
    val stars = remember {
        List(48) {
            StarSpec(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 1.8f + 0.6f,
                alpha = Random.nextFloat() * 0.55f + 0.25f
            )
        }
    }

    Canvas(modifier = modifier) {
        stars.forEach { star ->
            drawCircle(
                color = Color.White.copy(alpha = star.alpha),
                radius = star.radius,
                center = Offset(star.x * size.width, star.y * size.height)
            )
        }
    }
}

private data class StarSpec(
    val x: Float,
    val y: Float,
    val radius: Float,
    val alpha: Float
)
