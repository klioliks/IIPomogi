package ru.iipomogi.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import ru.iipomogi.app.ui.screens.HomeScreen
import ru.iipomogi.app.ui.screens.WebViewScreen
import ru.iipomogi.app.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                IiPomogiApp()
            }
        }
    }
}

private sealed interface AppScreen {
    data object Home : AppScreen
    data class Web(val url: String, val title: String) : AppScreen
}

@Composable
private fun IiPomogiApp() {
    var screen by rememberSaveable { mutableStateOf("home") }
    var webUrl by rememberSaveable { mutableStateOf("") }
    var webTitle by rememberSaveable { mutableStateOf("") }

    val current: AppScreen = when (screen) {
        "web" -> AppScreen.Web(webUrl, webTitle)
        else -> AppScreen.Home
    }

    when (val route = current) {
        is AppScreen.Home -> {
            HomeScreen(
                onOpenSection = { url, title ->
                    webUrl = url
                    webTitle = title
                    screen = "web"
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        is AppScreen.Web -> {
            WebViewScreen(
                url = route.url,
                title = route.title,
                onNavigateHome = { screen = "home" },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
