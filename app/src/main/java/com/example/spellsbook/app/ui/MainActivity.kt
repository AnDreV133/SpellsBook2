package com.example.spellsbook.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.spellsbook.app.ui.compose.MainWindow
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

@Composable
fun ComposeHolder(
    menuBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )
            menuBar()
        }
    ) {
        Box(
            modifier = Modifier
                .padding(it)
        ) {
            content()
        }
    }
}


