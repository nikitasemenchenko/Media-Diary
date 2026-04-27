package ru.magnum.mediadiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import ru.magnum.mediadiary.ui.MediaDiaryApp
import ru.magnum.mediadiary.ui.theme.MediaDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaDiaryTheme {
                MediaDiaryApp()
            }
        }
    }
}
