package ru.iipomogi.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.iipomogi.app.ui.theme.AppColors
import ru.iipomogi.app.ui.theme.AppShapes
import ru.iipomogi.app.ui.theme.AppTypography

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = AppShapes.Button,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            contentColor = AppColors.TextPrimary,
            disabledContentColor = AppColors.TextMuted
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(AppShapes.Button)
                .background(
                    if (enabled) {
                        AppColors.PrimaryButtonGradient
                    } else {
                        Brush.horizontalGradient(
                            listOf(
                                AppColors.AccentPurple.copy(alpha = 0.35f),
                                AppColors.AccentBlue.copy(alpha = 0.35f)
                            )
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, style = AppTypography.Button)
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .border(
                width = 1.dp,
                color = AppColors.ButtonSecondaryBorder,
                shape = AppShapes.Button
            ),
        shape = AppShapes.Button,
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.ButtonSecondary,
            contentColor = AppColors.TextPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
    ) {
        Text(text = text, style = AppTypography.Button)
    }
}

@Composable
fun TextLinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(onClick = onClick, modifier = modifier.fillMaxWidth()) {
        Text(
            text = text,
            style = AppTypography.Label.copy(color = AppColors.AccentLavender),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SoftBadge(
    text: String,
    modifier: Modifier = Modifier,
    leadingEmoji: String? = null
) {
    Row(
        modifier = modifier
            .clip(AppShapes.Badge)
            .background(AppColors.GlassDark)
            .border(1.dp, AppColors.CardBorder, AppShapes.Badge)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (leadingEmoji != null) {
            Text(text = leadingEmoji, style = AppTypography.Caption)
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = AppTypography.Caption.copy(color = AppColors.TextSecondary)
        )
    }
}

@Composable
fun GlowDot(modifier: Modifier = Modifier, color: Color = AppColors.AccentCyan) {
    Box(
        modifier = modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.9f))
    )
}
