package com.example.xotandroid.video.product

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.widget.ImageButton
import androidx.activity.compose.BackHandler
import androidx.annotation.FloatRange
import androidx.annotation.OptIn
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.SecureFlagPolicy
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.ForwardingPlayer
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.session.MediaSession
import androidx.media3.ui.PlayerView
import androidx.media3.ui.R
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.example.xotandroid.github_vid.settings.VideoPlayerCacheManager
import com.example.xotandroid.github_vid.settings.VideoPlayerControllerConfig
import com.example.xotandroid.github_vid.settings.VideoPlayerMediaItem
import com.example.xotandroid.github_vid.settings.applyToExoPlayerView
import com.example.xotandroid.github_vid.settings.findActivity
import com.example.xotandroid.github_vid.settings.setFullScreen
import com.example.xotandroid.github_vid.settings.toUri
import kotlinx.coroutines.delay
import java.util.UUID

@SuppressLint("SuspiciousIndentation")
@kotlin.OptIn(ExperimentalSharedTransitionApi::class)
@OptIn(UnstableApi::class)
@Composable
fun XotVideoPlayer(
    modifier: Modifier = Modifier,
    mediaItems: List<VideoPlayerMediaItem>,
    handleLifecycle: Boolean = true,
    autoPlay: Boolean = true,
    usePlayerController: Boolean = false,
    controllerConfig: VideoPlayerControllerConfig = VideoPlayerControllerConfig.Default,
    seekBeforeMilliSeconds: Long = 10000L,
    seekAfterMilliSeconds: Long = 10000L,
    @FloatRange(from = 0.0, to = 1.0) volume: Float = 1f,
    onCurrentTimeChanged: (Long) -> Unit = {},
    onFullScreenEnter: () -> Unit = {},
    onFullScreenExit: () -> Unit = {},
    defaultFullScreen: Boolean = false,
    handleAudioFocus: Boolean = true,
    playerBuilder: ExoPlayer.Builder.() -> ExoPlayer.Builder = { this },
    playerInstance: ExoPlayer.() -> Unit = {},
    navController: NavController,
    productViewModel: ProductViewModel
){
        val context = LocalContext.current
//        var currentTime by remember { mutableLongStateOf(0L) }
        var mediaSession = remember<MediaSession?> { null }

//        val player = remember {
//            val httpDataSourceFactory = DefaultHttpDataSource.Factory()
//            ExoPlayer.Builder(context)
//                .setSeekBackIncrementMs(seekBeforeMilliSeconds)
//                .setSeekForwardIncrementMs(seekAfterMilliSeconds)
//                .setAudioAttributes(
//                    AudioAttributes.Builder()
//                        .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
//                        .setUsage(C.USAGE_MEDIA)
//                        .build(),
//                    handleAudioFocus,
//                )
//                .apply {
//                    val cache = VideoPlayerCacheManager.getCache()
//                    if (cache != null) {
//                        val cacheDataSourceFactory = CacheDataSource.Factory()
//                            .setCache(cache)
//                            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context, httpDataSourceFactory))
//                        setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
//                    }
//                }
//                .playerBuilder()
//                .build()
//                .also(playerInstance)
//        }
        val player = productViewModel.exoPlayer

        val defaultPlayerView = remember {
            PlayerView(context)
        }
    BackHandler(enabled = true) {
        player.pause()
        navController.popBackStack()
    }
//        LaunchedEffect(Unit) {
//            while (true) {
//                delay(1000)
//                if (currentTime != player.currentPosition) {
//                    onCurrentTimeChanged(currentTime)
//                }
//                currentTime = player.currentPosition
//            }
//        }
        LaunchedEffect(usePlayerController) {
            defaultPlayerView.useController = usePlayerController
        }
        LaunchedEffect(player) {
            defaultPlayerView.player = player
        }
        LaunchedEffect(mediaItems, player) {
            mediaSession?.release()
            mediaSession = MediaSession.Builder(context, ForwardingPlayer(player))
                .setId(
                    "VideoPlayerMediaSession_${
                        UUID.randomUUID().toString().lowercase().split("-").first()
                    }"
                )
                .build()
            val exoPlayerMediaItems = mediaItems.map {
                val uri = it.toUri(context)
                MediaItem.Builder().apply {
                    setUri(uri)
                    setMediaMetadata(it.mediaMetadata)
                    setMimeType(it.mimeType)
                    setDrmConfiguration(
                        if (it is VideoPlayerMediaItem.NetworkMediaItem) {
                            it.drmConfiguration
                        } else {
                            null
                        },
                    )
                }.build()
            }
            player.setMediaItems(exoPlayerMediaItems)
            player.prepare()

            if (autoPlay) {
                player.play()
            }
        }
        var isFullScreenModeEntered by remember { mutableStateOf(defaultFullScreen) }
        LaunchedEffect(controllerConfig) {
            controllerConfig.applyToExoPlayerView(defaultPlayerView) {
                isFullScreenModeEntered = it
                if (it) {
                    onFullScreenEnter()
                }
            }
        }
        LaunchedEffect(volume) {
            player.volume = volume
        }
        Box(modifier = Modifier.clickable{
//            if (!isFullScreenModeEntered) {
//                isFullScreenModeEntered = true
//                val currentActivity = context.findActivity()
//                currentActivity.setFullScreen(true)
//                onFullScreenEnter()
//            }
            navController.navigate("full_screen_test")
        }
        ){
            VideoPlayerSurface(
                modifier = modifier,
                defaultPlayerView = defaultPlayerView,
                player = player,
                usePlayerController = usePlayerController,
                handleLifecycle = handleLifecycle,
            )
        }
//        if (isFullScreenModeEntered) {
//            var fullScreenPlayerView by remember { mutableStateOf<PlayerView?>(null) }
//            ProductFullScreen(
//                player = player,
//                currentPlayerView = defaultPlayerView,
//                controllerConfig = controllerConfig,
//                onDismissRequest = {
//                    fullScreenPlayerView?.let {
//                        PlayerView.switchTargetView(player, it, defaultPlayerView)
//                        defaultPlayerView.findViewById<ImageButton>(R.id.exo_fullscreen)
//                            .performClick()
//                        val currentActivity = context.findActivity()
//                        currentActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//                        currentActivity.setFullScreen(false)
//                        onFullScreenExit()
//                    }
//                    isFullScreenModeEntered = false
//                },
//                securePolicy = SecureFlagPolicy.Inherit,
//                fullScreenPlayerView = {
//                    fullScreenPlayerView = this
//                },
//            )
//        }
}

@SuppressLint("UnsafeOptInUsageError", "OpaqueUnitKey")
@Composable
internal fun VideoPlayerSurface(
    modifier: Modifier = Modifier,
    defaultPlayerView: PlayerView,
    player: ExoPlayer,
    usePlayerController: Boolean,
    handleLifecycle: Boolean,
    autoDispose: Boolean = true,
) {

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    AndroidView(
        modifier = modifier.background(androidx.compose.ui.graphics.Color.Black),
        factory = {
            defaultPlayerView.apply {
                useController = usePlayerController
                setBackgroundColor(android.graphics.Color.WHITE)
            }
        },
    )
    DisposableEffect(
    player
    ) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    if (handleLifecycle) {
                        player.pause()
                    }
                }
                Lifecycle.Event.ON_RESUME -> {
                    if (handleLifecycle) {
                        player.play()
                    }
                }
                else -> {}
            }
        }
        val lifecycle = lifecycleOwner.value.lifecycle
        lifecycle.addObserver(observer)

        onDispose {
            if (autoDispose) {
                player.pause()
                lifecycle.removeObserver(observer)
            }
        }
    }
}

