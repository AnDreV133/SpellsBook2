package com.example.spellsbook.db

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.spellsbook.data.store.AppDatabase

object DBInMemory {
    private var db: AppDatabase? = null

    fun open(): AppDatabase {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()

        return db!!
    }
}