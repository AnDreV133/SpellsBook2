package com.example.spellsbook.data.mapper

import com.example.spellsbook.data.store.entity.spells.SpellEntity
import com.example.spellsbook.data.store.entity.spells.SpellEntityEn
import com.example.spellsbook.data.store.entity.spells.SpellEntityRu
import com.example.spellsbook.domain.model.SpellDetailModel
import com.google.gson.Gson
import com.google.gson.JsonParser

// todo unified mappers for all languages

fun SpellDetailModel.mapToEntityEn(): SpellEntityEn =
    SpellEntityEn(
        name = this.name,
        uuid = this.uuid,
        json = Gson().toJson(this)
    )

fun SpellDetailModel.mapToEntityRu(): SpellEntityRu =
    SpellEntityRu(
        name = this.name,
        uuid = this.uuid,
        json = Gson().toJson(this)
    )

fun SpellEntity.mapToShortModel(): com.example.spellsbook.domain.model.SpellShortModel =
    JsonParser.parseString(this.json).asJsonObject.let { json ->
        com.example.spellsbook.domain.model.SpellShortModel(
            name = this.name,
            uuid = this.uuid,
            level = json["level"].asInt,
            school = json["school"].asString,
        )
    }


fun SpellEntity.mapToDetailModel(): SpellDetailModel =
    JsonParser.parseString(this.json).asJsonObject.let { json ->
        SpellDetailModel(
            name = this.name,
            uuid = this.uuid,
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
