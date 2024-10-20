package com.example.spellsbook.data.store

import android.app.Application
import androidx.room.Room
import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import com.example.spellsbook.data.store.entity.BookEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

object AppDatabaseConnection {

    @Volatile
    private var _db: AppDatabase? = null
    private const val DB_NAME = "app-database"
    private const val TAG = "AppDB"

    fun instance(
        appContext: Context
    ): AppDatabase {
        _db = _db ?: synchronized(this) {
            _db ?: Room.databaseBuilder(
                appContext as Application,
                AppDatabase::class.java,
                DB_NAME
            )
                .allowMainThreadQueries()
//                .addCallback(
//                    PreparingDatabaseCallback(appContext.resources)
//                )
                .setQueryCallback(
                    { sqlQuery, _ -> Log.d(TAG, "SQL Query: $sqlQuery") },
                    Executors.newSingleThreadExecutor()
                )
                .build() // todo remove allowMainThreadQueries()
        }

//        _db!!.query(
//            "insert into ${BookEntity.TABLE_NAME} " + "(${BookEntity.COLUMN_NAME}) " + "values ('test')",
//            null
//        )

//        _db!!.query("delete from ${BookEntity.TABLE_NAME} where ${BookEntity.COLUMN_ID} = 1", null)

//        "bd06ff73-1d3b-485d-8036-36e2bb17e403 LEVEL_0 CONJURATION"
//            .split(" ", limit = 3)
//            .also { fields ->
//                _db!!.query(
//                    "INSERT INTO ${TaggingSpellEntity.TABLE_NAME} " +
//                            "(${TaggingSpellEntity.COLUMN_UUID}, ${TaggingSpellEntity.COLUMN_LEVEL_TAG}, ${TaggingSpellEntity.COLUMN_SCHOOL_TAG}) " +
//                            "VALUES ('${fields[0]}', '${fields[1]}', '${fields[2]}')",
//                    null
//                )
//            }

        return _db!!
    }

    fun reinstantiation(
        appContext: Context
    ): AppDatabase {
        appContext.deleteDatabase(DB_NAME)
        _db = null

        return instance(appContext)
    }

//    private fun prePopulateDatabase(appContext: Context, db: AppDatabase) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val isDbHasData = _db!!.query(
//                """
//                select exists(
//                    select *
//                    from ${TaggingSpellEntity.TABLE_NAME}
//                    limit 1
//                )
//                """, null
//            ).run { moveToLast(); getInt(0) } != 0
//
////            if (!isDbHasData)
////                PreparingDatabaseCallback(appContext.resources).init()
//        }
//    }
}
