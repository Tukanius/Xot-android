package com.example.xotandroid.image

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
@OptIn(ExperimentalSharedTransitionApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SharedTransitionScope.ImageViewPage(
    navController: NavController,
    animatedVisibilityScope: AnimatedVisibilityScope
){
    val imageUrls = listOf(
        "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
        "https://i.imgur.com/CzXTtJV.jpg",
        "https://farm9.staticflickr.com/8295/8007075227_dc958c1fe6_z_d.jpg"
    )

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        imageUrls.forEachIndexed { index, url ->

                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp, 250.dp)
                                    .sharedElement(
                                        state = rememberSharedContentState(key = "image-$index"),
                                        animatedVisibilityScope = animatedVisibilityScope
                                    )
                                    .clickable {
                                        navController.navigate("full_screen_image/${Uri.encode(url)}/$index") {
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    },
                                contentScale = ContentScale.Crop
                            )
                    }
                }
            }
        }
    )
    // List of image URLs (4 images)
//    val imageUrls = listOf(
//        "https://farm2.staticflickr.com/1533/26541536141_41abe98db3_z_d.jpg",
//        "https://i.imgur.com/CzXTtJV.jpg",
//        "https://farm9.staticflickr.com/8295/8007075227_dc958c1fe6_z_d.jpg",
//        "https://farm4.staticflickr.com/3075/3168662394_7d7103de7d_z_d.jpg"  // The 4th image
//    )

//    with(sharedTransitionScope) {
//        Column(modifier = Modifier.fillMaxWidth()) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .horizontalScroll(rememberScrollState()),
//                horizontalArrangement = Arrangement.SpaceEvenly
//            ) {
//                imageUrls.forEachIndexed { index, url ->
//                    AsyncImage(
//                        model = url,
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(150.dp, 200.dp)
//                            .sharedElement(
//                                sharedTransitionScope.rememberSharedContentState(key = "image-$index"),
//                                animatedVisibilityScope = animatedContentScope
//                            )
//                            .clickable {
//                                val encodedUrl = Uri.encode(url)
//                                navController.navigate("full_screen_image/$encodedUrl/$index")
//                            },
//                        contentScale = ContentScale.Crop
//                    )
//                }
//            }
//        }
//    }
}
