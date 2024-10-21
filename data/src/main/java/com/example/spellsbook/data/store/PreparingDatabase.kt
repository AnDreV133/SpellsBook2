package com.example.spellsbook.data.store

import android.content.Context
import com.example.spellsbook.data.store.entity.SpellEntity
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
//            initSpells(appContext) { db.spellDao().insert(it) }
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
}

suspend fun initDbByLocale(
    context: Context,
    locale: String,
    db: AppDatabase,
) = withContext(Dispatchers.IO) {
    when (locale) {
        "en", "ru" -> Unit
        else -> {
            throw IllegalArgumentException()
        }
    }

    db.spellDao().deleteAll()

    ("spells_$locale.json" to locale)
        .also { fileAndLocale ->
            JsonParser
                .parseReader(context.assets.open(fileAndLocale.first).bufferedReader())
                .asJsonArray
                .forEach { jsonElem ->
                    launch {
                        (jsonElem as JsonObject).let { jsonObj ->
                            db.spellDao().insert(
                                SpellEntity(
                                    uuid = jsonObj.getString("uuid"),
                                    name = jsonObj.getString("name"),
                                    json = jsonObj.toString()
                                )
                            )
                        }
                    }
                }
        }
}

private fun JsonObject.getString(key: String) = this.get(key).asString