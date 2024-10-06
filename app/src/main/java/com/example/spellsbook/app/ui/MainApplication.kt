package com.example.spellsbook.app.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

open class CoreApplication : Application()

@HiltAndroidApp
class MainApplication : CoreApplication()

