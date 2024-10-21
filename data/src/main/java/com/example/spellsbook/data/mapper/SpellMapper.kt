package com.example.spellsbook.data.mapper

import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.display.SpellWithTagsShort
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.google.gson.JsonParser

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
