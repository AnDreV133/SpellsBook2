package com.example.spellsbook.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.spellsbook.app.ui.compose.fragments.MainWindow
import com.example.spellsbook.app.ui.compose.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MainWindow {
                AppNavHost()
            }
        }
    }
}
