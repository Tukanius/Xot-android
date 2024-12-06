package com.example.xotandroid.video

import PlayerViewModel
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Slider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.xotandroid.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay
import androidx.core.view.WindowCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.media3.ui.PlayerView.switchTargetView
import com.example.xotandroid.github_vid.settings.findActivity

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FullScreenVideo(
    navController: NavController,
    playerViewModel: PlayerViewModel
) {
    var isMuted by remember { mutableStateOf(playerViewModel.exoPlayer.volume == 0f) }
    var sliderPosition by remember { mutableFloatStateOf(0f) }
    val exoPlayer = playerViewModel.exoPlayer
    val videoDuration = exoPlayer.duration
    val systemUiController = rememberSystemUiController()
    val context = LocalContext.current

//    val currentPlayerView = remember {
//        PlayerView(context).apply {
//            player = playerViewModel.exoPlayer
//            useController = false
//        }
//    }

    val internalFullScreenPlayerView = remember {
        PlayerView(context).apply {
            player = playerViewModel.exoPlayer
            useController = false
        }
    }

//    LaunchedEffect(Unit) {
//        switchTargetView(exoPlayer, currentPlayerView, internalFullScreenPlayerView)
//    }

    // Update system UI and player
    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.White,
            darkIcons = true
        )
        WindowCompat.setDecorFitsSystemWindows((context as android.app.Activity).window, true)
    }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    exoPlayer.seekTo(0L)
                    exoPlayer.playWhenReady = true
                    exoPlayer.play()
                }
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    LaunchedEffect(exoPlayer) {
        exoPlayer.playWhenReady = true
    }

    LaunchedEffect(exoPlayer, isMuted) {
        exoPlayer.volume = if (isMuted) 0f else 1f
    }

    LaunchedEffect(exoPlayer) {
        while (true) {
            sliderPosition = if (videoDuration > 0) {
                exoPlayer.currentPosition.toFloat() / videoDuration.toFloat()
            } else {
                0f
            }
            delay(100)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = true
            )
        }
    }
    BackHandler() {
        navController.popBackStack()
    }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Gray)
            ) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize(),
//
                    factory = {
                        internalFullScreenPlayerView
                    },
                )
//                Image(
//                    painter = painterResource(
//                        id =
//                        if(isEnded){
//                            R.drawable.reset_svgrepo_com
//                        }else{
//                            if (isPlaying) R.drawable.pause_svgrepo_com else R.drawable.play_svgrepo_com
//                        }
//                    ),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(60.dp)
//                        .background(Color.Transparent)
//                        .align(Alignment.Center)
//                        .clickable {
//                            if (isEnded) {
//                                exoPlayer.seekTo(0L)
//                                exoPlayer.playWhenReady = true
//                                isEnded = false
//                            } else {
//                                if (isPlaying) {
//                                    exoPlayer.pause()
//                                } else {
//                                    exoPlayer.play()
//                                }
//                                isPlaying = !isPlaying
//                            }
//                        },
//                    contentScale = ContentScale.Fit
//                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    Image(
                        painter = painterResource(
                            id = if (isMuted) R.drawable.sound_mute_svgrepo_com else R.drawable.sound_up_svgrepo_com
                        ),
                        contentDescription = if (isMuted) "Unmute" else "Mute",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable {
                                isMuted = !isMuted
                                exoPlayer.volume = if (isMuted) 0f else 1f
                            },
                        contentScale = ContentScale.Fit
                    )
                    Slider(
                        value = sliderPosition,
                        onValueChange = { newValue ->
                            sliderPosition = newValue
                            val seekPosition = (videoDuration * sliderPosition).toLong()
                            exoPlayer.seekTo(seekPosition)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        colors = androidx.compose.material3.SliderDefaults.colors(
                            activeTrackColor = Color.White,
                            thumbColor = Color.White,
                            inactiveTrackColor = Color.Gray
                        )
                    )
                }
        }
}