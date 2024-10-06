package com.example.spellsbook.data.store

import android.content.Context
import com.example.spellsbook.data.R
import com.example.spellsbook.data.store.entity.spells.SpellEntityEn
import com.example.spellsbook.data.store.entity.spells.SpellEntityRu
import com.example.spellsbook.data.store.entity.tags.LevelEntity
import com.example.spellsbook.data.store.entity.tags.SchoolEntity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppDatabaseInitializer(
    private val appContext: Context,
    private val db: AppDatabase
) {

    suspend fun init(): Unit = withContext(Dispatchers.IO) {
        initDataEn()
        initDataRu()
    }

    private suspend fun initDataEn() = coroutineScope {
        val jsonParser =
            JsonParser.parseReader(inputStreamReaderFromResId(SOURCE_RES_LOCALE_EN)).asJsonArray
        for (jsonElement in jsonParser) {
            launch {
                val spell = jsonElement.asJsonObject
                val spellUuid = spell.getString("uuid")
                db.spellDaoEn().insert(
                    SpellEntityEn(
                        uuid = spellUuid,
                        name = spell.getString("name"),
                        json = spell.toString()
                    )
                )
                db.schoolDao().insert(
                    SchoolEntity(
                        tag = spell.getString("school").uppercase(),
                        spellUuid = spellUuid,
                    )
                )
                db.levelDao().insert(
                    LevelEntity(
                        tag = "LEVEL_${spell.getString("level")}",
                        spellUuid = spellUuid,
                    )
                )
                // todo for other inserts
            }
        }
    }

    private suspend fun initDataRu() = coroutineScope {
        val jsonParser =
            JsonParser.parseReader(inputStreamReaderFromResId(SOURCE_RES_LOCALE_RU)).asJsonArray
        for (jsonElement in jsonParser) {
            launch {
                val spell = jsonElement.asJsonObject
                db.spellDaoRu().insert(
                    SpellEntityRu(
                        uuid = spell.getString("uuid"),
                        name = spell.getString("name"),
                        json = spell.toString()
                    )
                )
            }
        }
    }

    private fun inputStreamReaderFromResId(resId: Int) =
        appContext.resources.openRawResource(resId).reader()

    private fun JsonObject.getString(key: String) =
        this.get(key).asString

    companion object {
        private val SOURCE_RES_LOCALE_EN = R.raw.spells_en
        private val SOURCE_RES_LOCALE_RU = R.raw.spells_ru
    }
}