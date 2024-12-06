import android.annotation.SuppressLint
import android.view.View
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.xotandroid.R
import com.example.xotandroid.github_vid.toPlayerViewResizeMode

@androidx.annotation.OptIn(UnstableApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun VideoViewPage(
    navController: NavController,
    playerViewModel: PlayerViewModel,
) {
    var isMuted by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val exoPlayer = playerViewModel.exoPlayer
    val defaultPlayerView = remember {
        PlayerView(context)
    }
    val configure by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(configure) {
        defaultPlayerView.useController = configure
    }
    LaunchedEffect(exoPlayer) {
        defaultPlayerView.player = exoPlayer
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
//    var isNavigating by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        exoPlayer.pause()
        navController.popBackStack()
    }
       Surface(
           modifier = Modifier
               .fillMaxSize()
       ) {
           Column(
               modifier = Modifier
                   .fillMaxSize()
                   .padding(16.dp),
           ) {
                   Box(
                       modifier = Modifier
                           .width(250.dp)
                           .height(320.dp)
                           .background(Color.Red),
                       contentAlignment = Alignment.Center
                   ) {
                       AndroidView(
                           modifier = Modifier
                               .fillMaxSize()
                               .clickable {
                                   navController.navigate("full_screen_video")
                               },
                           factory = {
                               defaultPlayerView
//                                        defaultPlayerView.apply {
//                                            hideController()
//
//                                            setShowNextButton(false)
//                                            setShowPreviousButton(false)
//                                            setShowFastForwardButton(false)
//                                            setShowRewindButton(false)
//
//                                            useController = false
//                                            controllerHideOnTouch = false
//                                            controllerAutoShow = false
//                                            layoutParams = android.view.ViewGroup.LayoutParams(
//                                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//                                                android.view.ViewGroup.LayoutParams.MATCH_PARENT
//                                            )
//
//                                        }
//                                        PlayerView(context).apply {
//                                            player = exoPlayer
//                                            useController = false
//                                            layoutParams = android.view.ViewGroup.LayoutParams(
//                                                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//                                                android.view.ViewGroup.LayoutParams.MATCH_PARENT
//                                            )
//                                        }
                           },
//                                    update = { view ->
//                                        view.player = exoPlayer
//                                        view.useController = false
//                                        view.controllerHideOnTouch = false
//                                        view.controllerAutoShow = false
//                                    }
                       )
//                            Image(
//                                painter = painterResource(
//                                    id =
//                                    if(isEnded){
//                                        R.drawable.reset_svgrepo_com
//                                    }else{
//                                        if (isPlaying) R.drawable.pause_svgrepo_com else R.drawable.play_svgrepo_com
//                                    }
//                                ),
//                                contentDescription = null,
//                                modifier = Modifier
//                                    .size(40.dp)
//                                    .background(Color.Transparent)
//                                    .align(Alignment.Center)
//                                    .clickable {
//                                        if (isEnded) {
//                                            exoPlayer.seekTo(0L)
//                                            exoPlayer.playWhenReady = true
//                                            isEnded = false
//                                        } else {
//                                            if (isPlaying) {
//                                                exoPlayer.pause()
//                                            } else {
//                                                exoPlayer.play()
//                                            }
//                                            isPlaying = !isPlaying
//                                        }
//                                    },
//                                contentScale = ContentScale.Fit
//                            )

                       Box(
                           modifier = Modifier
                               .size(40.dp)
                               .padding(10.dp)
                               .align(Alignment.BottomEnd)
                               .clickable {
                                   isMuted = !isMuted
                                   exoPlayer.volume = if (isMuted) 0f else 1f
                               }
                       ) {
                           Image(
                               painter = painterResource(
                                   id = if (isMuted) R.drawable.sound_mute_svgrepo_com else R.drawable.sound_up_svgrepo_com
                               ),
                               contentDescription = null,
                               modifier = Modifier.fillMaxSize(),
                               contentScale = ContentScale.Fit
                           )
                       }
               }
           }
       }

    }