package com.example.xotandroid

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ListPage() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.LightGray,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Text 1")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Text 2")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Text 3")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Text 4")
            }
        }
    )
}
