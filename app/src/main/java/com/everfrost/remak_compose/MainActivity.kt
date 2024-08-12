package com.everfrost.remak_compose

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import com.everfrost.remak_compose.ui.theme.Remak_composeTheme
import com.everfrost.remak_compose.ui.theme.white
import com.everfrost.remak_compose.view.RemakApp
import com.everfrost.remak_compose.view.RemakScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Remak_composeTheme {
                Surface(
                    color = white
                ) {
                    RemakApp(startDestination = RemakScreen.LinkDetail.route)
                }
            }
        }
    }
}

