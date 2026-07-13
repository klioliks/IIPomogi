package ru.iipomogi.app.navigation

import android.net.Uri

/**
 * Все URL разделов сайта в одном месте.
 * При необходимости замени маршруты здесь — больше нигде менять не нужно.
 *
 * Важно: для WebView используем только punycode-домен, не кириллический.
 */
object AppDestinations {
    /** Punycode-версия домена ии-помоги.рф — стабильнее для Android WebView. */
    const val BASE_URL = "https://xn----ftbnacwnbg.xn--p1ai"

    const val HOME_URL = "$BASE_URL/"

    /**
     * Ссылка для открытия сайта во внешнем браузере.
     * Кириллический домен в Chrome/Firefox обычно открывается нормально.
     */
    const val BROWSER_SITE_URL = "https://ии-помоги.рф/"

    /** ИИ-навигатор — отдельная страница бота. */
    const val NAVIGATOR_URL = "$BASE_URL/ai-navigator"

    /** Раздел «Выдохни». */
    const val PAUSE_URL = "$BASE_URL/pause"

    /** Профориентационный тест. */
    const val TEST_URL = "$BASE_URL/test"

    /** Личный кабинет. */
    const val ACCOUNT_URL = "$BASE_URL/account"

    /** Хосты сайта (для проверки внутренних ссылок). */
    val SITE_HOSTS = setOf(
        "xn----ftbnacwnbg.xn--p1ai",
        "www.xn----ftbnacwnbg.xn--p1ai",
        // На случай, если WebView покажет Unicode-host после редиректа.
        "ии-помоги.рф",
        "www.ии-помоги.рф"
    )

    /** Главная страница сайта (/), не раздел вроде /test или /pause. */
    fun isSiteHomeUrl(url: String?): Boolean {
        if (url.isNullOrBlank()) return false
        val uri = Uri.parse(url)
        val host = uri.host?.lowercase().orEmpty()
        val isOurHost = SITE_HOSTS.any {
            host == it.lowercase() || host.endsWith(".${it.lowercase()}")
        }
        if (!isOurHost) return false
        val path = uri.path?.trimEnd('/').orEmpty()
        return path.isEmpty()
    }
}
