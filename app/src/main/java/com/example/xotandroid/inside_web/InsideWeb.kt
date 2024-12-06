package com.example.xotandroid.inside_web
import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun WebViewPage(url: String, navController: NavHostController) {
    val decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8.name())
    val context = LocalContext.current
    val webView = remember { WebView(context) }
    var isLoading by remember { mutableStateOf(true) }
    var loadingProgress by remember { mutableStateOf(0) }

    BackHandler(enabled = webView.canGoBack()) {
        webView.goBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Web Page") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    AndroidView(
                        factory = {
                            webView.apply {
                                webViewClient = WebViewClient()
                                webChromeClient = CustomWebChromeClient(
                                    onProgressChanged = { progress ->
                                        loadingProgress = progress
                                        isLoading = progress < 100
                                    }
                                )
                                settings.javaScriptEnabled = true
                                loadUrl(decodedUrl)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                if (isLoading) {
                    LinearProgressIndicator(
                        progress = loadingProgress / 100f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(top = 8.dp)
                    )
                }
            }
        }
    )
}

