package ru.iipomogi.app.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
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
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import ru.iipomogi.app.BuildConfig
import ru.iipomogi.app.navigation.AppDestinations
import ru.iipomogi.app.ui.components.ErrorState
import ru.iipomogi.app.ui.components.LoadingState
import ru.iipomogi.app.ui.theme.AppColors
import ru.iipomogi.app.ui.theme.AppTypography

private const val WEB_LOG_TAG = "IIPomogiWebView"

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
    val targetUrl = remember(url) { url.trim() }
    val openedAsSiteHome = remember(targetUrl) {
        AppDestinations.isSiteHomeUrl(targetUrl)
    }
    var loadState by remember(targetUrl) { mutableStateOf(WebLoadState.Loading) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var reloadKey by remember(targetUrl) { mutableIntStateOf(0) }
    var shownUrl by remember(targetUrl) { mutableStateOf(targetUrl) }

    fun handleBack() {
        val webView = webViewRef
        if (webView != null && webView.canGoBack()) {
            webView.goBack()
        } else {
            onNavigateHome()
        }
    }

    fun leaveToAppHome(reason: String, fromUrl: String?) {
        Log.d(WEB_LOG_TAG, "Back to app home ($reason): $fromUrl")
        onNavigateHome()
    }

    BackHandler { handleBack() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        WebTopBar(
            title = title,
            debugUrl = if (BuildConfig.DEBUG) shownUrl else null,
            onBack = { handleBack() }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            key(targetUrl, reloadKey) {
                AndroidView(
                    modifier = Modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            // Чтобы поля логина/пароля принимали фокус и клавиатуру.
                            isFocusable = true
                            isFocusableInTouchMode = true
                            descendantFocusability = ViewGroup.FOCUS_AFTER_DESCENDANTS
                            importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_YES

                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            settings.useWideViewPort = true
                            settings.loadWithOverviewMode = false
                            settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                            settings.setSupportZoom(false)
                            settings.builtInZoomControls = false
                            settings.displayZoomControls = false
                            settings.cacheMode = WebSettings.LOAD_DEFAULT
                            settings.userAgentString =
                                "Mozilla/5.0 (Linux; Android 14; Pixel 7) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/120.0.0.0 Mobile Safari/537.36"

                            // Вертикальный скролл нужен; горизонтальный — нет.
                            isVerticalScrollBarEnabled = true
                            isHorizontalScrollBarEnabled = false
                            overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
                            setInitialScale(100)

                            CookieManager.getInstance().setAcceptCookie(true)
                            CookieManager.getInstance()
                                .setAcceptThirdPartyCookies(this, true)

                            // Не чистим cookies/storage при каждом открытии —
                            // иначе вход в кабинет сбрасывается и формы ведут себя странно.
                            if (BuildConfig.DEBUG) {
                                WebView.setWebContentsDebuggingEnabled(true)
                            }

                            webChromeClient = WebChromeClient()
                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(
                                    view: WebView?,
                                    pageUrl: String?,
                                    favicon: Bitmap?
                                ) {
                                    Log.d(WEB_LOG_TAG, "Page started: $pageUrl")
                                    if (pageUrl != null) shownUrl = pageUrl
                                    // Если открыли раздел (/test и т.п.), а сайт увёл на главную —
                                    // возвращаем в приложение.
                                    if (!openedAsSiteHome &&
                                        AppDestinations.isSiteHomeUrl(pageUrl)
                                    ) {
                                        leaveToAppHome("page-started-home", pageUrl)
                                        return
                                    }
                                    loadState = WebLoadState.Loading
                                }

                                override fun onPageFinished(view: WebView?, pageUrl: String?) {
                                    Log.d(WEB_LOG_TAG, "Page finished: $pageUrl")
                                    Log.d(WEB_LOG_TAG, "WebView.getUrl(): ${view?.url}")
                                    Log.d(WEB_LOG_TAG, "WebView.title: ${view?.title}")
                                    if (pageUrl != null) shownUrl = pageUrl
                                    if (!openedAsSiteHome &&
                                        AppDestinations.isSiteHomeUrl(pageUrl)
                                    ) {
                                        leaveToAppHome("page-finished-home", pageUrl)
                                        return
                                    }
                                    // Убираем старые инъекции, которые могли ломать скролл вниз.
                                    restorePageScrolling(view)
                                    if (loadState != WebLoadState.Error) {
                                        loadState = WebLoadState.Ready
                                    }
                                }

                                override fun doUpdateVisitedHistory(
                                    view: WebView?,
                                    pageUrl: String?,
                                    isReload: Boolean
                                ) {
                                    // SPA (TanStack) часто меняет URL через history API.
                                    if (!openedAsSiteHome &&
                                        AppDestinations.isSiteHomeUrl(pageUrl)
                                    ) {
                                        leaveToAppHome("history-home", pageUrl)
                                        return
                                    }
                                    if (pageUrl != null) shownUrl = pageUrl
                                    restorePageScrolling(view)
                                }

                                override fun onReceivedHttpError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    errorResponse: WebResourceResponse?
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        Log.d(
                                            WEB_LOG_TAG,
                                            "HTTP error ${errorResponse?.statusCode} for ${request.url}"
                                        )
                                    }
                                }

                                override fun onReceivedError(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                    error: WebResourceError?
                                ) {
                                    if (request?.isForMainFrame == true) {
                                        Log.d(
                                            WEB_LOG_TAG,
                                            "Page error: ${request.url} code=${error?.errorCode}"
                                        )
                                        loadState = WebLoadState.Error
                                    }
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    val uri = request?.url ?: return false
                                    val urlString = uri.toString()
                                    val host = uri.host?.lowercase().orEmpty()
                                    val isInternal = AppDestinations.SITE_HOSTS.any {
                                        host == it.lowercase() ||
                                            host.endsWith(".${it.lowercase()}")
                                    }

                                    // «Вернуться на главную» на сайте → главная приложения.
                                    if (isInternal &&
                                        !openedAsSiteHome &&
                                        AppDestinations.isSiteHomeUrl(urlString)
                                    ) {
                                        leaveToAppHome("link-home", urlString)
                                        return true
                                    }

                                    if (isInternal || host.isEmpty()) {
                                        Log.d(WEB_LOG_TAG, "Internal navigation: $uri")
                                        return false
                                    }
                                    return try {
                                        Log.d(WEB_LOG_TAG, "External browser: $uri")
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
                            Log.d(WEB_LOG_TAG, "Opening URL: $targetUrl")
                            loadUrl(
                                targetUrl,
                                mapOf(
                                    "Cache-Control" to "no-cache",
                                    "Pragma" to "no-cache"
                                )
                            )
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
    debugUrl: String?,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppColors.CosmicDeep)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
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
        if (debugUrl != null) {
            Text(
                text = debugUrl,
                color = AppColors.AccentCyan,
                fontSize = 10.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Убирает старые CSS-инъекции приложения, которые ломали вертикальный скролл.
 * Дальше оставляем вёрстку сайта как есть.
 */
private fun restorePageScrolling(webView: WebView?) {
    if (webView == null) return
    val js = """
        (function() {
          try {
            ['iipomogi-mobile-fit', 'iipomogi-scroll-fix'].forEach(function(id) {
              var el = document.getElementById(id);
              if (el && el.parentNode) el.parentNode.removeChild(el);
            });
            document.documentElement.style.removeProperty('overflow-x');
            document.documentElement.style.removeProperty('overflow-y');
            document.documentElement.style.removeProperty('height');
            if (document.body) {
              document.body.style.removeProperty('overflow-x');
              document.body.style.removeProperty('overflow-y');
              document.body.style.removeProperty('height');
              document.body.style.removeProperty('min-height');
              document.body.style.removeProperty('max-width');
              document.body.style.removeProperty('min-width');
            }
          } catch (e) {}
        })();
    """.trimIndent()
    webView.evaluateJavascript(js, null)
}
