package com.example.spellsbook.data.store

import android.content.res.Resources
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.spellsbook.data.R
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PreparingDatabase(
    private val appResources: Resources,
    private val db: AppDatabase
) {

    fun prepare() {
//        CoroutineScope(Dispatchers.IO).launch {
//            db.withTransaction {
        initTaggingSpells(db)
//                initSpells(db)
//            }
//        }
    }


    private fun initTaggingSpells(
        db: RoomDatabase
    ) {
        "bd06ff73-1d3b-485d-8036-36e2bb17e403 LEVEL_0 CONJURATION"
            .split(" ", limit = 3)
            .also { fields ->
                db.query(
                    "INSERT INTO ${TaggingSpellEntity.TABLE_NAME} " +
                            "(${TaggingSpellEntity.COLUMN_UUID}, ${TaggingSpellEntity.COLUMN_LEVEL_TAG}, ${TaggingSpellEntity.COLUMN_SCHOOL_TAG}) " +
                            "VALUES ('${fields[0]}', '${fields[1]}', '${fields[2]}')",
                    null
                )
            }
//        R.raw.tags.inputStreamReaderFromResId().forEachLine { line ->
//            line.split(" ", limit = 3).let { fields ->
////                launch {
//                    db.query(
//                        "INSERT INTO ${TaggingSpellEntity.TABLE_NAME} " +
//                                "(${TaggingSpellEntity.COLUMN_UUID}, ${TaggingSpellEntity.COLUMN_LEVEL_TAG}, ${TaggingSpellEntity.COLUMN_SCHOOL_TAG}) " +
//                                "VALUES ('${fields[0]}', '${fields[1]}', '${fields[2]}')",
//                        null
//                    )
////                }
//            }
//        }
    }

    private suspend fun initSpells(
        db: AppDatabase
    ) = withContext(Dispatchers.IO) {
        listOf(R.raw.spells_en to "en", R.raw.spells_ru to "ru").forEach { fileIdAndLocale ->
            JsonParser
                .parseReader(fileIdAndLocale.first.inputStreamReaderFromResId())
                .asJsonArray
                .forEach { jsonElem ->
                    launch {
                        (jsonElem as JsonObject).let { jsonObj ->
                            db.query(
                                "INSERT INTO ${SpellEntity.TABLE_NAME} " +
                                        "(${SpellEntity.COLUMN_UUID}, ${SpellEntity.COLUMN_LOCALE}, " +
                                        "${SpellEntity.COLUMN_NAME}, ${SpellEntity.COLUMN_JSON}) " +
                                        "VALUES ('${jsonObj.getString("uuid")}', '${fileIdAndLocale.second}', " +
                                        "'${jsonObj.getString("name")}', '$jsonObj');",
                                null
                            )
                        }
                    }
                }
        }
    }

    private fun Int.inputStreamReaderFromResId() =
        appResources.openRawResource(this).bufferedReader()

    private fun JsonObject.getString(key: String) =
        this.get(key).asString
}