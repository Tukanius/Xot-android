package com.example.xotandroid.inside_web

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun WebLinks(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val urls = listOf(
                "https://www.instagram.com/",
                "https://developer.android.com/guide",
                "https://www.facebook.com/photo/?fbid=10220987491555253&set=a.3283965436237",
                "https://www.youtube.com",
                "https://www.youtube.com/watch?v=5-nJ2kHgldI"
            )

            urls.forEach { url ->
                ClickableText(
                    text = AnnotatedString(url),
                    onClick = {
                        val encodedUrl = java.net.URLEncoder.encode(url, "UTF-8")
                        navController.navigate("webView/$encodedUrl")
                    },
                    style = androidx.compose.ui.text.TextStyle(
                        textDecoration = TextDecoration.Underline
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }

}