package ru.iipomogi.app.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.iipomogi.app.ui.theme.AppColors
import ru.iipomogi.app.ui.theme.AppShapes

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val clickModifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = AppShapes.Card,
                ambientColor = AppColors.AccentPurple.copy(alpha = 0.25f),
                spotColor = AppColors.AccentBlue.copy(alpha = 0.2f)
            )
            .border(
                width = 1.dp,
                color = AppColors.CardBorder,
                shape = AppShapes.Card
            )
            .then(clickModifier),
        shape = AppShapes.Card,
        color = AppColors.CardDark,
        contentColor = AppColors.TextPrimary
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 16.dp),
            content = content
        )
    }
}

@Composable
fun AppCardLight(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 6.dp, shape = AppShapes.Card)
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.12f),
                shape = AppShapes.Card
            ),
        shape = AppShapes.Card,
        color = Color.White.copy(alpha = 0.94f),
        contentColor = AppColors.CosmicDeep
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            content = content
        )
    }
}
