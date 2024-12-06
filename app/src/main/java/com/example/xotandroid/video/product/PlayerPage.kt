package com.example.xotandroid.video.product

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.xotandroid.github_vid.settings.VideoPlayerControllerConfig
import com.example.xotandroid.github_vid.settings.VideoPlayerMediaItem

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayerPage(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope,
    productViewModel: ProductViewModel
){
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content={
            Box(modifier = Modifier.fillMaxSize())
            {
                XotVideoPlayer(
                    productViewModel = productViewModel,
                    navController = navController,
                    mediaItems = listOf(
                        VideoPlayerMediaItem.NetworkMediaItem(
                            url = "https://www.exit109.com/~dnn/clips/RW20seconds_1.mp4",
                        ),
                        VideoPlayerMediaItem.NetworkMediaItem(
                            url = "https://dev-goodtech.s3.dualstack.ap-southeast-1.amazonaws.com/1400ff37-1519-482a-b885-cb4aa7e30942.mp4",
                        ),
                    ),
                    handleLifecycle = true,
                    autoPlay = true,
                    usePlayerController = false,
                    handleAudioFocus = true,
                    controllerConfig = VideoPlayerControllerConfig(
                        showSpeedAndPitchOverlay = false,
                        showSubtitleButton = false,
                        showCurrentTimeAndTotalTime = false,
                        showBufferingProgress = false,
                        showForwardIncrementButton = false,
                        showBackwardIncrementButton = false,
                        showBackTrackButton = false,
                        showNextTrackButton = false,
                        showRepeatModeButton = false,
                        controllerShowTimeMilliSeconds = 5_000,
                        controllerAutoShow = false,
                        showFullScreenButton = false,
                    ),
                    volume = 0.5f,
                    onCurrentTimeChanged = {
                        Log.e("CurrentTime", it.toString())
                    },
                    playerInstance = {
                        addAnalyticsListener(
                            object : AnalyticsListener {
                            }
                        )
                    },
                    modifier = Modifier
                        .size(250.dp,200.dp)
                        .align(Alignment.Center),
                )
            }
        }
    )
}
