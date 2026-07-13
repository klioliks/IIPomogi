package ru.iipomogi.app.navigation

/**
 * Все URL разделов сайта в одном месте.
 * При необходимости замени маршруты здесь — больше нигде менять не нужно.
 */
object AppDestinations {
    /** Punycode-версия домена ии-помоги.рф — стабильнее для Android WebView. */
    const val BASE_URL = "https://xn----ftbnacwnbg.xn--p1ai"

    const val HOME_URL = BASE_URL

    /** ИИ-навигатор — на главной странице сайта. */
    const val NAVIGATOR_URL = BASE_URL

    /** Раздел «Выдохни». */
    const val PAUSE_URL = "$BASE_URL/pause"

    /** Профориентационный тест. */
    const val TEST_URL = "$BASE_URL/test"

    /** Личный кабинет. */
    const val ACCOUNT_URL = "$BASE_URL/account"

    /** Хосты сайта (для проверки внутренних ссылок). */
    val SITE_HOSTS = setOf(
        "xn----ftbnacwnbg.xn--p1ai",
        "ии-помоги.рф",
        "www.xn----ftbnacwnbg.xn--p1ai"
    )
}
