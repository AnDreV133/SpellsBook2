package com.example.spellsbook.data.store

import android.app.Application
import androidx.room.Room
import android.content.Context
import com.example.spellsbook.data.store.entity.spells.SpellEntityEn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AppDatabaseConnection {

    @Volatile
    private var _db: AppDatabase? = null
    private const val DB_NAME = "app-database"

    fun instance(
        appContext: Context
    ): AppDatabase {
        _db = _db ?: synchronized(this) {
            _db ?: Room.databaseBuilder(
                appContext as Application,
                AppDatabase::class.java, DB_NAME
            ).allowMainThreadQueries().build().also { db -> // todo start on main thread
                prePopulateDatabase(appContext, db)

                _db = db
            }
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

    private fun prePopulateDatabase(appContext: Context, db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            val isDbHasData = _db!!.query(
                """
                select exists(
                    select *
                    from ${SpellEntityEn.TABLE_NAME}
                    limit 1
                )
                """, null
            ).run { moveToLast(); getInt(0) } != 0

            if (!isDbHasData)
                AppDatabaseInitializer(appContext, db).init()
        }
    }
}
