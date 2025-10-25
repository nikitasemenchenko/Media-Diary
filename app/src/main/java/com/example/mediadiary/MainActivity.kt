package com.example.mediadiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.mediadiary.ui.MediaDiaryApp
import com.example.mediadiary.ui.theme.MediaDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaDiaryTheme {
                MediaDiaryApp()
            }
        }
    }
}
