// PlayerViewModel.kt
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class PlayerViewModel(context: Context) : ViewModel() {
    val exoPlayer: ExoPlayer = ExoPlayer.Builder(context).build()

    init {
        val mediaItem = MediaItem.fromUri(Uri.parse("https://dev-goodtech.s3.dualstack.ap-southeast-1.amazonaws.com/de928b72-8397-4791-8753-f26b9341ddd5.mp4"))
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.playWhenReady = false
    }
    @SuppressLint("StaticFieldLeak")
    val playerView: PlayerView = PlayerView(context).apply {
        player = exoPlayer
        useController = false
    }
    fun getPlayer(): ExoPlayer {
        return exoPlayer
    }
    override fun onCleared() {
        super.onCleared()
        exoPlayer.release()
    }
}