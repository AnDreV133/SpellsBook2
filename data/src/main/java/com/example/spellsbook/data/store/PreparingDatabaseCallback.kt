package com.example.spellsbook.data.store

import android.content.res.Resources
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.spellsbook.data.R
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreparingDatabaseCallback(
    private val appResources: Resources
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        CoroutineScope(Dispatchers.IO).launch {
            initTaggingSpells(db).join()
            initSpells(db).join()
        }
    }

    private suspend fun initTaggingSpells(
        db: SupportSQLiteDatabase
    ) = withContext(Dispatchers.IO) {
        launch {
            R.raw.tags.inputStreamReaderFromResId().forEachLine { line ->
                line.split(" ", limit = 3).let { fields ->
                    launch {
                        db.query(
                            "INSERT INTO ${TaggingSpellEntity.TABLE_NAME} " +
                                    "(${TaggingSpellEntity.COLUMN_UUID}, ${TaggingSpellEntity.COLUMN_LEVEL_TAG}, ${TaggingSpellEntity.COLUMN_SCHOOL_TAG}) " +
                                    "VALUES ('${fields[0]}', '${fields[1]}', '${fields[2]}');"
                        )
                    }
                }
            }
        }
    }

    private suspend fun initSpells(db: SupportSQLiteDatabase) = withContext(Dispatchers.IO) {
        launch {
            listOf(R.raw.spells_en to "en", R.raw.spells_ru to "ru").forEach { fileIdAndLocale ->
                JsonParser
                    .parseReader(fileIdAndLocale.first.inputStreamReaderFromResId())
                    .asJsonArray
                    .forEach { jsonElem ->
                        launch {
                            (jsonElem as JsonObject).let { jsonObj ->
                                db.query(
                                    "INSERT INTO ${SpellEntity.TABLE_NAME} " +
                                            "(${SpellEntity.COLUMN_SPELL_UUID}, ${SpellEntity.COLUMN_LOCALE}, " +
                                            "${SpellEntity.COLUMN_NAME}, ${SpellEntity.COLUMN_JSON}) " +
                                            "VALUES ('${jsonObj.getString("uuid")}', '${fileIdAndLocale.second}', " +
                                            "'${jsonObj.getString("name")}', '$jsonObj');",
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun Int.inputStreamReaderFromResId() =
        appResources.openRawResource(this).reader()

    private fun JsonObject.getString(key: String) =
        this.get(key).asString
}