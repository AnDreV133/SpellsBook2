package com.example.spellsbook.data.store

import android.content.Context
import com.example.spellsbook.data.store.dao.InitDao
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


internal suspend fun initDb(
    context: Context,
    initDao: InitDao
) = withContext(Dispatchers.IO) {
    initTaggingSpells(context, initDao)
    initSpellsWithLocalisation(context, initDao)
}

private suspend fun initTaggingSpells(
    context: Context,
    initDao: InitDao
) = withContext(Dispatchers.IO) {
    context.assets
        .open("tags.r")
        .bufferedReader()
        .useLines { lines ->
            lines.map { line ->
                line.trim()
                    .split(" ")
                    .let { fields ->
                        TaggingSpellEntity(
                            uuid = fields[0],
                            levelTag = fields[1],
                            schoolTag = fields[2]
                        )
                    }
            }.toList()
        }.also { taggingSpells ->
            initDao.clearAndInsertTaggingSpells(taggingSpells)
        }
}


private suspend fun initSpellsWithLocalisation(
    context: Context,
    initDao: InitDao
) = withContext(Dispatchers.IO) {
    listOf(
        "spells_ru.json" to "ru",
        "spells_en.json" to "en"
    ).map { fileAndLocale ->
        JsonParser
            .parseReader(context.assets.open(fileAndLocale.first).bufferedReader())
            .asJsonArray to fileAndLocale.second
    }.map { jsonArrayAndLocale->
        jsonArrayAndLocale.first.map { jsonElem ->
            (jsonElem as JsonObject).let { jsonObj ->
                SpellEntity(
                    uuid = jsonObj.getString("uuid"),
                    name = jsonObj.getString("name"),
                    language = jsonArrayAndLocale.second,
                    json = jsonObj.toString()
                )
            }
        }
    }.also { spells ->
        initDao.clearAndInsertSpells(spells.flatten())
    }
}

//suspend fun checkOnEmptyTable(initDao: InitDao) = initDao.hasSpells()


private fun JsonObject.getString(key: String) = this.get(key).asString