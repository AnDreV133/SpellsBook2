package com.example.spellsbook.data.mapper

import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.domain.enums.TagNameEnum

fun TagNameEnum.mapToColumnName(): String =
    when (this) {
        TagNameEnum.LEVEL -> TaggingSpellEntity.COLUMN_LEVEL_TAG
        TagNameEnum.SCHOOL -> TaggingSpellEntity.COLUMN_SCHOOL_TAG
        else -> throw IllegalArgumentException("This tagname is not supported")
    }