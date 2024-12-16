package com.example.spellsbook.data.store

import android.app.Application
import androidx.room.Room
import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object AppDatabaseConnection {
    @Volatile
    private var _db: AppDatabase? = null
    private const val DB_NAME = "app-databases"
    private const val TAG = "AppDB"

    fun instance(
        appContext: Context
    ): AppDatabase {
        _db = _db ?: synchronized(this) {
            _db ?: Room.databaseBuilder(
                appContext as Application,
                AppDatabase::class.java,
                DB_NAME
            ).addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                initDb(appContext, _db!!.initDao())
                            }
                        }
                    }
                )
                .setQueryCallback(
                    { sqlQuery, _ -> Log.d(TAG, "SQL Query: $sqlQuery") },
                    Executors.newSingleThreadExecutor()
                )
                .build()
        }

        return _db!!
    }

    fun reinstantiation(
        appContext: Context
    ): AppDatabase {
        appContext.deleteDatabase(DB_NAME)
        _db = null

        return instance(appContext)
    }
}
