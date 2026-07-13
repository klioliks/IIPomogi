package ru.iipomogi.app.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import ru.iipomogi.app.navigation.AppDestinations
import ru.iipomogi.app.ui.components.ErrorState
import ru.iipomogi.app.ui.components.LoadingState
import ru.iipomogi.app.ui.theme.AppColors
import ru.iipomogi.app.ui.theme.AppTypography

private enum class WebLoadState {
    Loading,
    Ready,
    Error
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    url: String,
    title: String,
    onNavigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    var loadState by remember { mutableStateOf(WebLoadState.Loading) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var reloadKey by remember { mutableIntStateOf(0) }

    fun handleBack() {
        val webView = webViewRef
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            onNavigateHome()
        }
    }

    BackHandler { handleBack() }

    Column(modifier = modifier.fillMaxSize()) {
        WebTopBar(
            title = title,
            onBack = { handleBack() }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            key(reloadKey) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                            settings.mixedContentMode =
                                WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = true
                            settings.setSupportZoom(true)
                            settings.builtInZoomControls = true
                            settings.displayZoomControls = false

                            CookieManager.getInstance().setAcceptCookie(true)
                            CookieManager.getInstance()
                                .setAcceptThirdPartyCookies(this, true)

                            webChromeClient = WebChromeClient()
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView?,
                                    pageUrl: String?,
                                    favicon: Bitmap?
                                ) {
                                    loadState = WebLoadState.Loading
                                }

                                override fun onPageFinished(view: WebView?, pageUrl: String?) {
                                    if (loadState != WebLoadState.Error) {
                                        loadState = WebLoadState.Ready
                                    }
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        loadState = WebLoadState.Error
                                    }
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    val uri = request?.url ?: return false
                                    val host = uri.host?.lowercase().orEmpty()
                                    val isInternal = AppDestinations.SITE_HOSTS.any {
                                        host == it.lowercase() || host.endsWith(".${it.lowercase()}")
                                    }
                                    if (isInternal || host.isEmpty()) return false
                                    return try {
                                        view?.context?.startActivity(
                                            Intent(Intent.ACTION_VIEW, uri)
                                        )
                                        true
                                    } catch (_: Exception) {
                                        false
                                    }
                                }
                            }

                            webViewRef = this
                            loadUrl(url)
                        }
                    },
                    update = { webView ->
                        webViewRef = webView
                    }
                )
            }

            when (loadState) {
                WebLoadState.Loading -> {
                    LoadingState(modifier = Modifier.fillMaxSize())
                }
                WebLoadState.Error -> {
                    ErrorState(
                        onRetry = {
                            loadState = WebLoadState.Loading
                            reloadKey++
                        },
                        onGoHome = onNavigateHome,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                WebLoadState.Ready -> Unit
            }
        }
    }
}

@Composable
private fun WebTopBar(
    title: String,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.CosmicDeep)
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = "←",
            style = AppTypography.Headline,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .clickable(onClick = onBack)
                .padding(8.dp)
        )
        Text(
            text = title,
            style = AppTypography.Title,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
