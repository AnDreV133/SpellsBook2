package com.example.spellsbook.app.ui

import android.app.Application
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

open class CoreApplication : Application()

@HiltAndroidApp
class MainApplication : CoreApplication() {

//    @Inject lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()

//        "bd06ff73-1d3b-485d-8036-36e2bb17e403 LEVEL_0 CONJURATION"
//            .split(" ", limit = 3)
//            .also { fields ->
//                db.query(
//                    "INSERT INTO ${TaggingSpellEntity.TABLE_NAME} " +
//                            "(${TaggingSpellEntity.COLUMN_UUID}, ${TaggingSpellEntity.COLUMN_LEVEL_TAG}, ${TaggingSpellEntity.COLUMN_SCHOOL_TAG}) " +
//                            "VALUES ('${fields[0]}', '${fields[1]}', '${fields[2]}')",
//                    null
//                )
//            }
    }
}

