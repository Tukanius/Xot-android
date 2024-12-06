@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.example.xotandroid

import FullScreenImagePage
import ImageVideoGalleryScreen
import PlayerViewModel
import ProductViewModelFactory
import VideoViewPage
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.xotandroid.file_upload.PhotoPicker
import com.example.xotandroid.image.ImageViewPage
import com.example.xotandroid.inside_web.WebLinks
import com.example.xotandroid.inside_web.WebViewPage
import com.example.xotandroid.test_video.TestingVidePage
import com.example.xotandroid.video.FullScreenVideo
import com.example.xotandroid.video.PlayerViewModelFactory
import com.example.xotandroid.video.product.FullScreenTest
import com.example.xotandroid.video.product.PlayerPage
import com.example.xotandroid.video.product.ProductViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            NavigationComponent()
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun NavigationComponent() {
    SharedTransitionLayout {

        val navController = rememberNavController()
        val playerViewModel: PlayerViewModel = viewModel(
            factory = PlayerViewModelFactory(LocalContext.current)
        )
        val playermodel: ProductViewModel = viewModel(
            factory = ProductViewModelFactory(LocalContext.current)
        )

        NavHost(
            navController = navController,
            startDestination = "home_page",
        ) {
            composable("home_page") { HomePage(navController) }
            composable("list_page") { ListPage() }
            composable("image_page") { ImageViewPage(
                navController,
                this
            )}
            composable("full_screen_image/{url}/{index}") { backStackEntry ->
                val url = backStackEntry.arguments?.getString("url")
                val index = backStackEntry.arguments?.getString("index")?.toInt() ?: 0

                val imageUrls = listOf(
                    "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
                    "https://i.imgur.com/CzXTtJV.jpg",
                    "https://farm9.staticflickr.com/8295/8007075227_dc958c1fe6_z_d.jpg"
                )
                FullScreenImagePage(
                    navController,
                    Uri.decode(url),
                    this,
                    index,
                    imageUrls
                )
            }
            composable("video_page") { VideoViewPage(navController, playerViewModel) }
            composable(
                "full_screen_video",
                enterTransition = {
                    fadeIn(animationSpec = tween(durationMillis = 100))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(durationMillis = 200))
                },
            ) {
                FullScreenVideo(navController, playerViewModel)
            }

            composable("file_upload_page") { ImageVideoGalleryScreen() }
            composable ("yt_page"){PhotoPicker(navController,this)  }
            composable("webLinks") { WebLinks(navController) }
            composable("webView/{url}") { backStackEntry ->
                val url = backStackEntry.arguments?.getString("url")
                url?.let { WebViewPage(url = it,navController) }
            }
            composable("testing"){TestingVidePage()}

            composable("player_page") { PlayerPage(navController,  this@composable, productViewModel = playermodel) }
            composable("full_screen_test",
                enterTransition = {
                    fadeIn(animationSpec = tween(300))
                },
                exitTransition = {
                    fadeOut(animationSpec = tween(300))
                },) { FullScreenTest(productViewModel = playermodel)}
        }
    }
}
@Composable
fun HomePage(navController: NavController) {
    val noAnimationOptions = NavOptions.Builder()
        .setEnterAnim(0)
        .setExitAnim(0)
        .setPopEnterAnim(0)
        .setPopExitAnim(0)
        .build()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("list_page",noAnimationOptions)}) {
            Text("Button to List test")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { navController.navigate("image_page",noAnimationOptions)}) {
            Text("Button to Main Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigate("video_page",noAnimationOptions)}) {
            Text("Button to Main Video")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigate("player_page",noAnimationOptions)}) {
            Text("Button to Test Video")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigate("file_upload_page",noAnimationOptions)}) {
            Text("Button to Image picker Test")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigate("yt_page",noAnimationOptions)}) {
            Text("Button to Youtube Test Image picker")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {navController.navigate("webLinks",noAnimationOptions)}) {
            Text("Button to Web links")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("testing",noAnimationOptions) }) {
            Text("Button to Test for every thing")
        }
    }
}
@Composable
fun SharedElementExample() {
    val transition = rememberInfiniteTransition(label = "")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .background(Color.Red)
    )
}