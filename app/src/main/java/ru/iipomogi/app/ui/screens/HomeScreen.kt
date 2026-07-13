package ru.iipomogi.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.iipomogi.app.R
import ru.iipomogi.app.navigation.AppDestinations
import ru.iipomogi.app.ui.components.AppCard
import ru.iipomogi.app.ui.components.CosmicBackground
import ru.iipomogi.app.ui.components.SecondaryButton
import ru.iipomogi.app.ui.components.SoftBadge
import ru.iipomogi.app.ui.theme.AppColors
import ru.iipomogi.app.ui.theme.AppTypography

data class HomeSection(
    val emoji: String,
    val title: String,
    val description: String,
    val url: String
)

private val homeSections = listOf(
    HomeSection(
        emoji = "🧭",
        title = "ИИ-навигатор",
        description = "Поможет выбрать, как использовать ИИ под твою задачу",
        url = AppDestinations.NAVIGATOR_URL
    ),
    HomeSection(
        emoji = "🐱",
        title = "Выдохни",
        description = "Антистресс-пауза, созвездия, картинки и линии спокойствия",
        url = AppDestinations.PAUSE_URL
    ),
    HomeSection(
        emoji = "✨",
        title = "Профориентация",
        description = "Короткий тест, чтобы увидеть близкие направления",
        url = AppDestinations.TEST_URL
    ),
    HomeSection(
        emoji = "👤",
        title = "Личный кабинет",
        description = "Прогресс, заметки и дорожная карта",
        url = AppDestinations.ACCOUNT_URL
    )
)

@Composable
fun HomeScreen(
    onOpenSection: (url: String, title: String) -> Unit,
    onOpenSiteInBrowser: () -> Unit,
    modifier: Modifier = Modifier
) {
    CosmicBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            SoftBadge(
                text = "Учиться с ИИ — умно и безопасно",
                leadingEmoji = "✦"
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "ИИ-помоги",
                        style = AppTypography.Display
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Учёба, ИИ и спокойный старт — в одном месте",
                        style = AppTypography.Title.copy(color = AppColors.AccentLavender)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.mascot_robot),
                    contentDescription = "Маскот ИИ-помоги",
                    modifier = Modifier.size(96.dp),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Выбери, с чего начать: разобраться с учёбой, выдохнуть, пройти тест или открыть личный кабинет.",
                style = AppTypography.Body
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "ИИ помогает, но думать всё равно тебе.",
                style = AppTypography.BodySmall.copy(color = AppColors.AccentCyan)
            )

            Spacer(modifier = Modifier.height(24.dp))

            homeSections.forEach { section ->
                AppCard(
                    onClick = { onOpenSection(section.url, section.title) }
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier.size(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = section.emoji, style = AppTypography.Headline)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = section.title, style = AppTypography.Title)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = section.description, style = AppTypography.BodySmall)
                        }
                        Text(
                            text = "→",
                            style = AppTypography.Title.copy(color = AppColors.AccentLavender)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            SecondaryButton(
                text = "Перейти на сайт ↗",
                onClick = onOpenSiteInBrowser
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Выдохни. Один шаг за раз.",
                style = AppTypography.Caption,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
