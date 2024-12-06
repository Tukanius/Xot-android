import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SharedTransitionScope.FullScreenImagePage(
    navController: NavController,
    url: String?,
    animatedVisibilityScope: AnimatedVisibilityScope,
    index: Int,
    imageUrls: List<String>
) {
    val pagerState = rememberPagerState(pageCount = { imageUrls.size }, initialPage = index)


    val systemUiController = rememberSystemUiController()

    val scale = remember { Animatable(1f) }
    val offsetX = remember { Animatable(0f) }
    val offsetY = remember { Animatable(0f) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        systemUiController.setStatusBarColor(color = Color.Black, darkIcons = false)
        systemUiController.setNavigationBarColor(color = Color.Black, darkIcons = false)
    }

    // Reset colors when leaving the screen
    DisposableEffect(Unit) {
        onDispose {
            systemUiController.setStatusBarColor(color = Color.White, darkIcons = true)
            systemUiController.setNavigationBarColor(color = Color.White, darkIcons = true)
        }
    }
//    HorizontalMultiBrowseCarousel(
//        state = carouselState,
//        preferredItemWidth = 0.dp
//    ){
//        page ->
//        Box(modifier = Modifier.size(300.dp)) {
//            Image(
//                painter = painterResource(
//                    id = when (page) {
//                        0 -> R.drawable.capp_1
//                        1 -> R.drawable.capp_2
//                        else -> R.drawable.capp_3
//                    }
//                ),
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )
//        }
//    }
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            content = {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .clickable { navController.popBackStack() }
                        ) {
                                AsyncImage(
                                    model = imageUrls[page],
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize()
                                        .sharedElement(
                                            state = rememberSharedContentState(key = "image-$index"),
                                            animatedVisibilityScope = animatedVisibilityScope
                                        ),
                                    contentScale = ContentScale.Fit
                                )
                        }
                    }
                }
            }
        )
    }
//    with(sharedTransitionScope){
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            content = {
//                Box(modifier = Modifier.fillMaxSize().background(Color.Black)){
//                    HorizontalPager(
//                        state = pagerState,
//                        modifier = Modifier.fillMaxSize()
//                    ) { page ->
//                        AsyncImage(
//                            model = imageUrls[page],
//                            contentDescription = null,
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .sharedElement(
//                                    sharedTransitionScope.rememberSharedContentState(key = "image-$index"),
//                                    animatedVisibilityScope = animatedContentScope
//                                )
//                                .clickable {
//                                    // Close the full-screen pager
//                                    navController.popBackStack()
//                                },
//                            contentScale = ContentScale.Fit
//                        )
//                    }
//                }
//
//            }
//        )
////        Box(
////            modifier = Modifier
////                .fillMaxSize()
////                .background(Color.Black),
////            contentAlignment = Alignment.Center
////        ) {
////            BoxWithConstraints(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .aspectRatio(1280f / 959f)
////            ) {
////                val state = rememberTransformableState { zoomChange, panChange, _ ->
////                    coroutineScope.launch {
////                        // Update scale, constraining within bounds
////                        val newScale = (scale.value * zoomChange).coerceIn(1f, 5f)
////
////                        val extraWidth = (newScale - 1) * constraints.maxWidth / 2
////                        val extraHeight = (newScale - 1) * constraints.maxHeight / 2
////
////                        val newOffsetX = (offsetX.value + panChange.x * newScale).coerceIn(-extraWidth, extraWidth)
////                        val newOffsetY = (offsetY.value + panChange.y * newScale).coerceIn(-extraHeight, extraHeight)
////
////                        // Snap values immediately or animate back to center if zooming out
////                        if (newScale == 1f) {
////                            scale.animateTo(1f, animationSpec = tween(300))
////                            offsetX.animateTo(0f, animationSpec = tween(300))
////                            offsetY.animateTo(0f, animationSpec = tween(300))
////                        } else {
////                            scale.snapTo(newScale)
////                            offsetX.snapTo(newOffsetX)
////                            offsetY.snapTo(newOffsetY)
////                        }
////                    }
////                }
////
////                AsyncImage(
////                    model = url,
////                    contentDescription = null,
////                    contentScale = ContentScale.Fit,
////                    modifier = Modifier
////                        .fillMaxSize()
////                        .sharedElement(
////                            sharedTransitionScope.rememberSharedContentState(key = "image-$index"),
////                            animatedVisibilityScope = animatedContentScope
////                        )
////                        .graphicsLayer {
////                        scaleX = scale.value
////                        scaleY = scale.value
////                        translationX = offsetX.value
////                        translationY = offsetY.value
////                    }
////                        .pointerInput(Unit) {
////                            detectTransformGestures { _, pan, zoom, _ ->
////                                coroutineScope.launch {
////                                    val newScale = (scale.value * zoom).coerceIn(1f, 5f)
////
////                                    val extraWidth = (newScale - 1) * constraints.maxWidth / 2
////                                    val extraHeight = (newScale - 1) * constraints.maxHeight / 2
////
////                                    val newOffsetX = (offsetX.value + pan.x * newScale).coerceIn(-extraWidth, extraWidth)
////                                    val newOffsetY = (offsetY.value + pan.y * newScale).coerceIn(-extraHeight, extraHeight)
////
////                                    // Snap values or animate back to center if zooming out
////                                    if (newScale == 1f) {
////                                        scale.animateTo(1f, animationSpec = tween(300))
////                                        offsetX.animateTo(0f, animationSpec = tween(300))
////                                        offsetY.animateTo(0f, animationSpec = tween(300))
////                                    } else {
////                                        scale.snapTo(newScale)
////                                        offsetX.snapTo(newOffsetX)
////                                        offsetY.snapTo(newOffsetY)
////                                    }
////                                }
////                            }
////                        }
////                        .transformable(state)
////                )
////            }
////
////            // Back button
////            Icon(
////                imageVector = Icons.Default.ArrowBack,
////                contentDescription = null,
////                modifier = Modifier
////                    .padding(16.dp)
////                    .align(Alignment.TopStart)
////                    .clickable {
////                        navController.popBackStack()
////                    },
////                tint = Color.White
////            )
////        }
//    }
