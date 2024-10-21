package com.example.spellsbook.data.store

import android.content.Context
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.SpellEntityRu
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object PreparingDatabase {
    fun prepare(appContext: Context, db: AppDatabase) {
        CoroutineScope(Dispatchers.IO).launch {
            initTaggingSpells(appContext) { db.taggingSpellDao().insert(it) }
            initSpells(appContext, "spells_en.json" to "en") { db.spellDao().insert(it) }
            initSpells(appContext, "spells_ru.json" to "ru") {
                db.spellDao().insert(
                    SpellEntityRu(
                        uuid = it.uuid,
                        name = it.name,
                        json = it.json
                    )
                )
            }
        }
    }


    private suspend fun initTaggingSpells(
        context: Context,
        insert: suspend (TaggingSpellEntity) -> Unit
    ) = withContext(Dispatchers.IO) {
        context.assets
            .open("tags.r")
            .bufferedReader()
            .useLines { lines ->
                lines.forEach { line ->
                    line.split(" ", limit = 3).also { fields ->
                        insert(
                            TaggingSpellEntity(
                                uuid = fields[0],
                                levelTag = fields[1],
                                schoolTag = fields[2]
                            )
                        )
                    }
                }
            }
    }


    private suspend fun initSpells(
        context: Context,
        fileAndLocale: Pair<String, String>,
        insert: suspend (SpellEntity) -> Unit
    ) = withContext(Dispatchers.IO) {
//        listOf(
//            "spells_en.json" to "en",
//            "spells_ru.json" to "ru"
//        ).forEach { fileAndLocale ->
        JsonParser
            .parseReader(context.assets.open(fileAndLocale.first).bufferedReader())
            .asJsonArray
            .forEach { jsonElem ->
                launch {
                    (jsonElem as JsonObject).let { jsonObj ->
                        insert(
                            SpellEntity(
                                uuid = jsonObj.getString("uuid"),
                                name = jsonObj.getString("name"),
                                json = jsonObj.toString()
                            )
                        )
                    }
                }
            }
//        }
    }

    private fun JsonObject.getString(key: String) = this.get(key).asString
}