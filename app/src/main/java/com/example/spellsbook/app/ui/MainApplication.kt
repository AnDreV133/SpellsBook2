package com.example.spellsbook.app.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

open class CoreApplication : Application()

val LAST_LOCALE_KEY = "last_locale"

@HiltAndroidApp
class MainApplication : CoreApplication() {
    override fun onCreate() {
        super.onCreate()

        getSharedPreferences(LAST_LOCALE_KEY, MODE_PRIVATE)
            .edit().putString(LAST_LOCALE_KEY, "").apply()
    }
}

