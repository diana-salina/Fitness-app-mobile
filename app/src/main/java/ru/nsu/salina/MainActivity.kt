package ru.nsu.salina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.nsu.salina.ui.navigation.AppNavigation
import ru.nsu.salina.ui.theme.HarmonieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val app = application as HarmonieApp
        setContent {
            HarmonieTheme {
                AppNavigation(repository = app.repository)
            }
        }
    }
}
