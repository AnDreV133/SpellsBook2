package com.example.spellsbook.app.ui.compose.fragments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.spellsbook.app.ui.theme.SpellsBookTheme

@Composable
fun MainWindow(content: @Composable () -> Unit) {
    SpellsBookTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}

@Composable
fun ScreenWithMenuBar(
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
                    .background(color = Color.Gray)
            ) {
                menuBar()
            }
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

//@Composable
//fun MainScreen(
//    navController: NavHostController,
//    content: @Composable () -> Unit
//) {
//    ScreenWithMenuBar(
//        menuBar = { MainMenuBar(navController) },
//        content = content
//    )
//}