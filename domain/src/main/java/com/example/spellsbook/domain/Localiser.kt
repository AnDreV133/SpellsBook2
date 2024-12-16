package com.example.spellsbook.domain

import android.content.res.AssetManager
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import org.json.JSONArray
import java.io.Closeable
import java.io.File
import javax.inject.Inject

class Localiser @Inject constructor(
    private val locale: LocaleEnum,
    private val assets: AssetManager
) : Closeable {
    private fun getSpellsJson(): JSONArray {
        val coreSpells  = JSONArray(assets.open(
            when (locale) {
                LocaleEnum.ENGLISH -> "spells_en.json"
                LocaleEnum.RUSSIAN -> "spells_ru.json"
            }
        ).bufferedReader().readText())

        val authorSpells = JSONArray(assets.open("author_spells.json").bufferedReader().readText())        

    }

    val coreSpellsJson = getLocaleFile()


    fun getByUuid(uuid: String): SpellDetailModel {

    }

    override fun close() {

    }
}

fun SpellWithTagsShort.mapToShortModel(): SpellShortModel =
    SpellShortModel(
        name = this.spell.name,
        uuid = this.spell.uuid,
        level = this.taggingSpell.levelTag?.let { LevelEnum.valueOf(it) },
    )

fun SpellEntity.mapToDetailModel(): SpellDetailModel =
    JsonParser.parseString(this.json).asJsonObject.let { json ->
        SpellDetailModel(
            name = json["name"].asString,
            level = json["level"].asInt,
            school = json["school"].asString,
            description = json["description"].asString,
            components = json["components"].asString,
            duration = json["duration"].asString,
            range = json["range"].asString,
            castingTime = json["castingTime"].asString,
            materials = json["materials"].asString,
            source = json["source"].asString,
        )
    }
