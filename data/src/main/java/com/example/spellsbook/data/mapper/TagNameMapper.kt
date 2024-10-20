package com.example.spellsbook.data.mapper

import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.domain.enums.TagIndentifierEnum

fun TagIndentifierEnum.mapToColumnName(): String =
    when (this) {
        TagIndentifierEnum.LEVEL -> TaggingSpellEntity.COLUMN_LEVEL_TAG
        TagIndentifierEnum.SCHOOL -> TaggingSpellEntity.COLUMN_SCHOOL_TAG
        else -> throw IllegalArgumentException("This tagname is not supported")
    }