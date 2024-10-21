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
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.spellsbook.app.ui.compose.MainWindow
import com.example.spellsbook.app.ui.compose.navigation.AppNavHost
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.data.store.PreparingDatabase
import com.example.spellsbook.data.store.initDbByLocale
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val locale = when (Locale.current.language) {
            "ru" -> "ru"
            else -> "en"
        }

        getSharedPreferences("global", MODE_PRIVATE).run {
            getString(LAST_LOCALE_KEY, "")?.also { lastLocale ->
                if (lastLocale != locale) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        initDbByLocale(this@MainActivity, locale, db)
                    }
                }
            }

            edit().putString(LAST_LOCALE_KEY, locale).apply()
        }

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


