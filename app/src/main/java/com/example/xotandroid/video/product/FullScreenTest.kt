package com.example.xotandroid.video.product

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.core.view.WindowCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.xotandroid.github_vid.settings.VideoPlayerControllerConfig
import com.example.xotandroid.github_vid.settings.applyToExoPlayerView
import com.example.xotandroid.github_vid.settings.findActivity
import com.example.xotandroid.github_vid.settings.setFullScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

@SuppressLint("UnsafeOptInUsageError", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FullScreenTest(
//    player: ExoPlayer,
//    currentPlayerView: PlayerView,
//    fullScreenPlayerView: PlayerView.() -> Unit,
//    controllerConfig: VideoPlayerControllerConfig,
    productViewModel: ProductViewModel
) {
    val context = LocalContext.current
    val internalFullScreenPlayerView = remember {
        PlayerView(context).apply {
            player = productViewModel.exoPlayer
        }
    }
    var isFullScreenModeEntered by remember {
        mutableStateOf(true)
    }
//    var visible by remember { mutableStateOf(false) }
//    LaunchedEffect(Unit) {
//        visible = true
//    }
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.White,
            darkIcons = true
        )
        WindowCompat.setDecorFitsSystemWindows((context as android.app.Activity).window, true)
    }
//    LaunchedEffect(Unit) {
//        PlayerView.switchTargetView(productViewModel.exoPlayer, productViewModel.defaultPlayerView, internalFullScreenPlayerView)
//        val currentActivity = context.findActivity()
//        currentActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//    }
    LaunchedEffect(internalFullScreenPlayerView) {
        internalFullScreenPlayerView.useController = false
    }
    val activityWindow = getActivityWindowTest()

//    SideEffect {
//        if (activityWindow != null  && !isFullScreenModeEntered) {
//            activityWindow.setFullScreen(true)
//            WindowManager.LayoutParams().apply {
//                copyFrom(activityWindow.attributes)
//            }
//            activityWindow.statusBarColor = Color.Black.toArgb()
//            isFullScreenModeEntered = true
//        }
//    }


//    LaunchedEffect(controllerConfig) {
//        controllerConfig.applyToExoPlayerView(internalFullScreenPlayerView) {
//        }
////        internalFullScreenPlayerView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_fullscreen)
////            .performClick()
//    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.LightGray,
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                VideoPlayerSurface(
                    defaultPlayerView = internalFullScreenPlayerView.apply { useController = false},
                    player = productViewModel.exoPlayer,
                    usePlayerController = false,
                    handleLifecycle = false,
                    autoDispose = false,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .align(Alignment.Center)
                )
            }
        }
    )


}



@Composable
internal fun getActivityWindowTest(): Window? = LocalView.current.context.findActivity().window