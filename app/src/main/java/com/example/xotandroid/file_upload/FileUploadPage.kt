
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import java.io.File

@Composable
fun ImageVideoGalleryScreen() {
    val context = LocalContext.current
    var imageBitmaps by remember { mutableStateOf<List<Bitmap>>(emptyList()) }
    var videoUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraUri?.let {
                val bitmap = getBitmapFromUri(it, context)
                bitmap?.let { img ->
                    imageBitmaps = imageBitmaps + img
                }
            }
        }
    }

    val getMediaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri?> ->
        uris.forEach { uri ->
            uri?.let {
                when {
                    it.toString().contains("image") -> {
                        val bitmap = getBitmapFromUri(it, context)
                        bitmap?.let { img ->
                            imageBitmaps = imageBitmaps + img
                        }
                    }
                    it.toString().contains("video") -> {
                        videoUris = videoUris + it
                    }
                }
            }
        }
    }

    val imageFile = File(context.cacheDir, "temp_image.jpg")
    cameraUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            cameraUri?.let { uri ->
                takePictureLauncher.launch(uri)
            }
        }) {
            Text(text = "Camera")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { getMediaLauncher.launch("*/*") }) {
            Text(text = "Gallery ?")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(imageBitmaps) { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                )
            }
            items(videoUris) { uri ->
                VideoPlayer(videoUri = uri)
            }
        }
        Spacer(modifier = Modifier.height(25.dp))
    }
}

@Composable
fun VideoPlayer(videoUri: Uri) {
    val context = LocalContext.current
    val exoplayer = remember { ExoPlayer.Builder(context).build() }
    DisposableEffect(Unit) {
        val mediaItem = MediaItem.fromUri(videoUri)
        exoplayer.setMediaItem(mediaItem)
        exoplayer.prepare()
        exoplayer.playWhenReady = false

        onDispose {
            exoplayer.release()
        }
    }
    AndroidView(
        factory = { PlayerView(context).apply { player = exoplayer } },
        modifier = Modifier.fillMaxWidth().height(200.dp)
    )
}

private fun getBitmapFromUri(uri: Uri, context: Context): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val drawable = Drawable.createFromStream(inputStream, uri.toString())
        drawable?.toBitmap()
    } catch (e: Exception) {
        null
    }
}