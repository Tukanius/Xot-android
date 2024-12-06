package com.example.xotandroid.test_video
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TestingVidePage() {
    var showEmojiPicker by remember { mutableStateOf(false) }
    var selectedEmoji by remember { mutableStateOf("🙂") }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = selectedEmoji, fontSize = 40.sp)
            Button(onClick = { showEmojiPicker = true }) {
                Text("Show Emojis")
            }
            if (showEmojiPicker) {
                EmojiPicker { emoji ->
                    selectedEmoji = emoji
                    showEmojiPicker = false
                }
            }
        }
    }
}

@Composable
fun EmojiPicker(onEmojiSelected: (String) -> Unit) {
    val emojiCategories = listOf(
        "Smileys" to listOf("😀", "😁", "😂", "🤣", "😃", "😄", "🥶", "😆"),
        "Animals" to listOf("🐶", "🐱", "🐭", "🐹", "🐰", "🦊", "🐻", "🐼"),
        "Food" to listOf("🍎", "🍊", "🍌", "🍉", "🍇", "🍓", "🍒", "🍑"),
        "Activities" to listOf("⚽", "🏀", "🏈", "⚾", "🎾", "🎱", "🏓", "🏸"),
        "Travel" to listOf("🚗", "🚕", "🚙", "🚌", "🚎", "🚑", "🚒", "🚓"),
        "Objects" to listOf("💡", "🔑", "🔒", "🔓", "📱", "📲", "💻", "🖥️"),
        "Symbols" to listOf("❤️", "💔", "❣️", "💕", "💞", "💓", "💗", "💖"),
        "Flags" to listOf("🏳️", "🏴", "🏁", "🚩", "🇺🇸", "🇨🇦", "🇬🇧", "🇯🇵"),
        "Custom" to listOf("\uD83E\uDEE0", "\uD83E\uDEF1\uD83C\uDFFC\u200D\uD83E\uDEF2\uD83C\uDFFF","\uD83E\uDEF0\uD83C\uDFFD","\uD83E\uDDA9")
    )

    Dialog(onDismissRequest = { }) {
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            emojiCategories.forEach { (category, emojis) ->
                item {
                    Text(
                        text = category,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier
                            .height(120.dp)
                            .padding(bottom = 16.dp)
                    ) {
                        items(emojis.size) { index ->
                            Text(
                                text = emojis[index],
                                fontSize = 24.sp,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable { onEmojiSelected(emojis[index]) }
                            )
                        }
                    }
                }
            }
        }
    }
}
