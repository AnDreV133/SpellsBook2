package com.example.spellsbook.data.mapper

import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.data.store.entity.model.SpellWithTagsShort
import com.example.spellsbook.domain.enums.CastingTimeEnum
import com.example.spellsbook.domain.enums.LevelEnum
import com.example.spellsbook.domain.enums.RangeEnum
import com.example.spellsbook.domain.enums.RitualEnum
import com.example.spellsbook.domain.enums.SchoolEnum
import com.example.spellsbook.domain.enums.SourceEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.model.SpellTagsModel
import com.google.gson.JsonObject
import com.google.gson.JsonParser

fun SpellWithTagsShort.mapToShortModel(): SpellShortModel =
    SpellShortModel(
        name = this.name,
        uuid = this.taggingSpell.uuid,
        level = this.taggingSpell.levelTag?.let { LevelEnum.valueOf(it) },
    )

fun SpellEntity.mapToDetailModel(): SpellDetailModel =
    JsonParser.parseString(this.json).asJsonObject.let { json ->
        SpellDetailModel(
            name = json.getFromJsonToString("name"),
            uuid = json.getFromJsonToString("uuid"),
            level = json.getFromJsonToString("level"),
            school = json.getFromJsonToString("school"),
            description = json.getFromJsonToString("description"),
            components = json.getFromJsonToString("components"),
            duration = json.getFromJsonToString("duration"),
            range = json.getFromJsonToString("range"),
            castingTime = json.getFromJsonToString("castingTime"),
            materials = json.getFromJsonToString("materials"),
            source = json.getFromJsonToString("source"),
        )
    }

 fun TaggingSpellEntity.mapToTagsModel(): SpellTagsModel =
     SpellTagsModel(
         level = levelTag?.let { LevelEnum.valueOf(it) },
         castingTime = castingTime?.let { CastingTimeEnum.valueOf(it) },
         school = schoolTag?.let {SchoolEnum.valueOf(it)},
         range = rangeTag?.let { RangeEnum.valueOf(it) },
         source = sourceTag?.let {SourceEnum.valueOf(it)},
         ritual = ritualTag?.let { RitualEnum.valueOf(it)}
     )


fun SpellDetailModel.mapToJson(): JsonObject =
    JsonObject().apply {
        addProperty("name", name)
        addProperty("uuid", uuid)
        addProperty("level", level)
        addProperty("school", school)
        addProperty("description", description)
        addProperty("components", components)
        addProperty("duration", duration)
        addProperty("range", range)
        addProperty("castingTime", castingTime)
        addProperty("materials", materials)
        addProperty("source", source)
    }

private fun JsonObject.getFromJsonToString(key: String): String =
    this.get(key).let {
        if (it != null) it.asString
        else ""
    }
