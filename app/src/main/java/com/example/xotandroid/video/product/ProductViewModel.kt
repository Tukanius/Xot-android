package com.example.xotandroid.video.product

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import com.example.xotandroid.github_vid.settings.VideoPlayerCacheManager

@SuppressLint("StaticFieldLeak")
class ProductViewModel(context: Context) : ViewModel() {
    private val httpDataSourceFactory = DefaultHttpDataSource.Factory()
     @UnstableApi
     val exoPlayer : ExoPlayer =
        ExoPlayer.Builder(context)
            .setSeekBackIncrementMs(1000)
            .setSeekForwardIncrementMs(1000)
            .apply {
                val cache = VideoPlayerCacheManager.getCache()
                if (cache != null) {
                    val cacheDataSourceFactory = CacheDataSource.Factory()
                        .setCache(cache)
                        .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context, httpDataSourceFactory))
                    setMediaSourceFactory(DefaultMediaSourceFactory(cacheDataSourceFactory))
                }
            }
            .build()

    init {
        val mediaItem = MediaItem.fromUri(Uri.parse("https://www.exit109.com/~dnn/clips/RW20seconds_1.mp4"))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = false
    }

    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}