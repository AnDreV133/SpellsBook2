package com.example.spellsbook.config

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.spellsbook.app.ui.CoreApplication
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltTestApplication

//@CustomTestApplication(CoreApplication::class)
//interface TestApplication

class HiltTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        name: String?,
        context: Context?
    ): Application {
        return super.newApplication(
            cl,
            HiltTestApplication::class.java.name,
            context
        )
    }
}