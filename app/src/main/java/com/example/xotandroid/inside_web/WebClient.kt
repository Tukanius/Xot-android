package com.example.xotandroid.inside_web

import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Toast

class CustomWebChromeClient(
    private val onProgressChanged: (Int) -> Unit
) : WebChromeClient() {

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        onProgressChanged(newProgress)
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: android.webkit.JsResult?
    ): Boolean {
        Toast.makeText(view?.context, message, Toast.LENGTH_LONG).show()
        result?.confirm()
        return true
    }
}
