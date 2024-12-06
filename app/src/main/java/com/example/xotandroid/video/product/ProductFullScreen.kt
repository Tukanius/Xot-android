package com.example.xotandroid.video.product
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.view.Window
import android.view.WindowManager
import android.widget.ImageButton
import androidx.activity.compose.BackHandler
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
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.xotandroid.github_vid.settings.VideoPlayerControllerConfig
import com.example.xotandroid.github_vid.settings.applyToExoPlayerView
import com.example.xotandroid.github_vid.settings.findActivity
import com.example.xotandroid.github_vid.settings.setFullScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

/**
 * ExoPlayer does not support full screen views by default.
 * So create a full screen modal that wraps the Compose Dialog.
 *
 * Delegate all functions of the video controller that were used just before
 * the full screen to the video controller managed by that component.
 * Conversely, if the full screen dismissed, it will restore all the functions it delegated
 * for synchronization with the video controller on the full screen and the video controller on the previous screen.
 *
 * @param player Exoplayer instance.
 * @param currentPlayerView [androidx.media3.ui.PlayerView] instance currently in use for playback.
 * @param fullScreenPlayerView Callback to return all features to existing video player controller.
 * @param controllerConfig Player controller config. You can customize the Video Player Controller UI.
 * @param onDismissRequest Callback that occurs when modals are closed.
 * @param securePolicy Policy on setting [android.view.WindowManager.LayoutParams.FLAG_SECURE] on a full screen dialog window.
 */
@SuppressLint("UnsafeOptInUsageError")
@Composable
fun ProductFullScreen(
    player: ExoPlayer,
    currentPlayerView: PlayerView,
    fullScreenPlayerView: PlayerView.() -> Unit,
    controllerConfig: VideoPlayerControllerConfig,
    onDismissRequest: () -> Unit,
    securePolicy: SecureFlagPolicy,
) {
        val context = LocalContext.current
        val internalFullScreenPlayerView = remember {
            PlayerView(context)
                .also(fullScreenPlayerView)
        }
        var isFullScreenModeEntered by remember {
            mutableStateOf(true)
        }
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            visible = true
        }
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(Unit) {
        systemUiController.setSystemBarsColor(
            color = Color.White,
            darkIcons = true
        )
        WindowCompat.setDecorFitsSystemWindows((context as android.app.Activity).window, true)
    }
    LaunchedEffect(visible) {
        if (!visible) {
            val activity = context.findActivity()
            activity.window?.statusBarColor = Color.Transparent.toArgb()
            delay(300)
            onDismissRequest()
        }
    }

    LaunchedEffect(Unit) {
        PlayerView.switchTargetView(player, currentPlayerView, internalFullScreenPlayerView)
        val currentActivity = context.findActivity()
        currentActivity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }
    LaunchedEffect(internalFullScreenPlayerView) {
        internalFullScreenPlayerView.useController = false
    }
    val activityWindow = getActivityWindow()
    val dialogWindow = getDialogWindow()
    SideEffect {
        if (activityWindow != null && dialogWindow != null && !isFullScreenModeEntered) {
            activityWindow.setFullScreen(true)
            dialogWindow.setFullScreen(true)
            WindowManager.LayoutParams().apply {
                copyFrom(activityWindow.attributes)
                type = dialogWindow.attributes.type
                dialogWindow.attributes = this
            }
            activityWindow.statusBarColor = Color.Black.toArgb()
            isFullScreenModeEntered = true
        }
    }


    LaunchedEffect(controllerConfig) {
        controllerConfig.applyToExoPlayerView(internalFullScreenPlayerView) {
            if (!it) {
                onDismissRequest()
            }
        }
        internalFullScreenPlayerView.findViewById<ImageButton>(androidx.media3.ui.R.id.exo_fullscreen)
            .performClick()
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            initialOffsetX = { it * 20 },
            animationSpec = tween(durationMillis = 100)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { it },
            animationSpec = tween(durationMillis = 250)
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                VideoPlayerSurface(
                    defaultPlayerView = internalFullScreenPlayerView,
                    player = player,
                    usePlayerController = false,
                    handleLifecycle = false,
                    autoDispose = false,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Text 4")
            }

        }
    }

//        Dialog(
//            onDismissRequest = {
//                visible = false
//            },
//            properties = DialogProperties(
//                dismissOnClickOutside = false,
//                usePlatformDefaultWidth = false,
//                securePolicy = securePolicy,
//                decorFitsSystemWindows = true,
//            ),
//            ) {
//
//
//        }

}


@Composable
internal fun getDialogWindow(): Window? = (LocalView.current.parent as? DialogWindowProvider)?.window

@Composable
internal fun getActivityWindow(): Window? = LocalView.current.context.findActivity().window