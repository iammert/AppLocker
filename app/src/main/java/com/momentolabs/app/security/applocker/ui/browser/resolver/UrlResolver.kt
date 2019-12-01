package com.momentolabs.app.security.applocker.ui.browser.resolver

object UrlResolver {

    private const val HTTP = "http://"

    const val FACEBOOK = "https://facebook.com"
    const val YOUTUBE = "https://youtube.com"
    const val TWITTER = "https://twitter.com"
    const val INSTAGRAM = "https://instagram.com"

    fun resolveUrl(url: String): String {
        //google.com -> http://google.com
        if (containsDot(url) && startWithProtocol(url).not()) {
            return HTTP + url
        }

        //http://...google.com -> original
        if (containsDot(url) && startWithProtocol(url)) {
            return url
        }

        return searchOnGoogle(url)
    }

    private fun containsDot(url: String): Boolean {
        return url.contains(".")
    }

    private fun startWithProtocol(url: String): Boolean {
        return url.startsWith("http") || url.startsWith("https")
    }

    private fun searchOnGoogle(url: String): String = "https://www.google.com/search?q=$url"
}