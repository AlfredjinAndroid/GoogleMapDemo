package com.jxd.mapdemo.widget


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.webkit.WebSettingsCompat
import androidx.webkit.WebViewFeature


/**
 * @author : Alfred
 * WebView Component 封装
 * 基于原Android WebView
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebComponent(
    url: String,
    enableJavascript: Boolean = true,
    javascriptInterface: Map<String, Any> = mapOf(),
    onPageFinished: ((view: WebView, url: String) -> Unit)? = null,
    onPageStarted: ((view: WebView, url: String) -> Unit)? = null,
    config: (WebView) -> Unit = {},
) {
    var progress by remember { mutableStateOf(0f) }

    val isLight = MaterialTheme.colors.isLight

    Box {
        if (progress != 1f) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart)
                    .zIndex(1f),
                progress = progress,
            )
        }
        Column(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                modifier = Modifier.weight(1f),
                factory = { context ->
                    WebView(context).also {
                        it.overScrollMode = View.OVER_SCROLL_NEVER
                        it.setBackgroundColor(0)
                        it.settings.apply {
                            javaScriptEnabled = enableJavascript
                        }
                        it.webChromeClient = ComposeWebChromeClient(
                            onProgress = { _progress ->
                                progress = _progress
                            }
                        )
                        it.webViewClient = ComposeWebViewClient(onPageFinished, onPageStarted)
                        javascriptInterface.forEach { item ->
                            it.addJavascriptInterface(item.value, item.key)
                        }
                        config.invoke(it)
                        it.setUrl(url)
                    }
                },
                update = {
                    if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                        val mode = if (isLight) WebSettingsCompat.FORCE_DARK_OFF else WebSettingsCompat.FORCE_DARK_ON
                        WebSettingsCompat.setForceDark(it.settings, mode)
                    }
                }
            )
        }
    }
}


private fun WebView.setUrl(url: String) {
    if (originalUrl != url) {
        loadUrl(url)
    }
}

private class ComposeWebChromeClient(
    private val onProgress: (Float) -> Unit = {},
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        onProgress.invoke(newProgress.toFloat() / 100f)
    }
}

private class ComposeWebViewClient(
    val pageFinished: ((view: WebView, url: String) -> Unit)? = null,
    val pageStarted: ((view: WebView, url: String) -> Unit)? = null,
) : WebViewClient() {

    override fun onPageFinished(view: WebView, url: String) {
        pageFinished?.invoke(view, url)
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        pageStarted?.invoke(view, url)
    }
}

