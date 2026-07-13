package ru.iipomogi.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ru.iipomogi.app.ui.theme.AppTypography

@Composable
fun ErrorState(
    title: String = "Не получилось открыть страницу",
    message: String = "Проверь интернет и попробуй ещё раз.",
    onRetry: () -> Unit,
    onGoHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    CosmicBackground(modifier = modifier, showNebulaImage = false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "✨",
                style = AppTypography.Display
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = title,
                style = AppTypography.Headline,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = message,
                style = AppTypography.Body,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(28.dp))
            PrimaryButton(text = "Повторить", onClick = onRetry)
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(text = "На главный экран", onClick = onGoHome)
        }
    }
}
