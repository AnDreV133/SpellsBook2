package com.example.spellsbook.data.store

import android.content.Context
import com.example.spellsbook.data.store.dao.InitDao
import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


internal suspend fun initDbOnStart(
    context: Context,
    initDao: InitDao
) = withContext(Dispatchers.IO) {
    context.assets
        .open("tags.r")
        .bufferedReader()
        .useLines { lines ->
            lines.map { line ->
                line.split(" ", limit = 3)
                    .let { fields ->
                        TaggingSpellEntity(
                            uuid = fields[0],
                            levelTag = fields[1],
                            schoolTag = fields[2]
                        )
                    }
            }.toList()
        }.also { taggingSpells ->
            initDao.insertTaggingSpells(taggingSpells)
        }
}


suspend fun initDbByLocale(
    context: Context,
    locale: String,
    initDao: InitDao,
) = withContext(Dispatchers.IO) {
    when (locale) {
        "en", "ru" -> Unit
        else -> {
            throw IllegalArgumentException()
        }
    }

    ("spells_$locale.json" to locale).let { fileAndLocale ->
        JsonParser
            .parseReader(context.assets.open(fileAndLocale.first).bufferedReader())
            .asJsonArray
    }.map { jsonElem ->
        (jsonElem as JsonObject).let { jsonObj ->
            SpellEntity(
                uuid = jsonObj.getString("uuid"),
                name = jsonObj.getString("name"),
                json = jsonObj.toString()
            )
        }
    }.also { spells ->
        initDao.clearAndInsertSpells(spells)
    }

}

suspend fun checkOnEmptyTable(initDao: InitDao) = initDao.hasSpells()


private fun JsonObject.getString(key: String) = this.get(key).asString