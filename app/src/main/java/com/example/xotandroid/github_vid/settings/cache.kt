package com.example.xotandroid.github_vid.settings

/*
 * Copyright 2023 Dora Lee
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/
import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

/**
 * Manage video player cache.
 */
object VideoPlayerCacheManager {

    @UnstableApi
    private lateinit var cacheInstance: Cache

    @SuppressLint("UnsafeOptInUsageError")
    fun initialize(context: Context, maxCacheBytes: Long) {
        if (::cacheInstance.isInitialized) {
            return
        }

        cacheInstance = SimpleCache(
            File(context.cacheDir, "video"),
            LeastRecentlyUsedCacheEvictor(maxCacheBytes),
            StandaloneDatabaseProvider(context),
        )
    }

    @OptIn(UnstableApi::class)
    internal fun getCache(): Cache? =
        if (::cacheInstance.isInitialized) {
            cacheInstance
        } else {
            null
        }
}
//Test
//object VideoPlayerCacheManager {
//
//    private const val MAX_CACHE_SIZE: Long = 100 * 1024 * 1024 // 100 MB
//    private const val CACHE_DIRECTORY_NAME = "media_cache"
//
//    private var simpleCache: SimpleCache? = null
//
//    fun initialize(context: Context) {
//        if (simpleCache == null) {
//            val cacheDir = File(context.cacheDir, CACHE_DIRECTORY_NAME)
//            simpleCache = SimpleCache(cacheDir, LeastRecentlyUsedCacheEvictor(MAX_CACHE_SIZE))
//        }
//    }
//
//    fun getCache(): SimpleCache? {
//        return simpleCache
//    }
//
//    fun releaseCache() {
//        simpleCache?.release()
//        simpleCache = null
//    }
//}