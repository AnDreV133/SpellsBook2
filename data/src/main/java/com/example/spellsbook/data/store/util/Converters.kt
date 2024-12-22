package com.example.spellsbook.data.store.util

import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum

internal fun TagIdentifierEnum.toColumnName() = when (this) {
    TagIdentifierEnum.LEVEL -> TaggingSpellEntity.COLUMN_LEVEL_TAG
    TagIdentifierEnum.SCHOOL -> TaggingSpellEntity.COLUMN_SCHOOL_TAG
    TagIdentifierEnum.CASTING_TIME -> TaggingSpellEntity.COLUMN_CASTING_TIME_TAG
    TagIdentifierEnum.RANGE -> TaggingSpellEntity.COLUMN_RANGE_TAG
    TagIdentifierEnum.RITUAL -> TaggingSpellEntity.COLUMN_RITUAL_TAG
    TagIdentifierEnum.SOURCE -> TaggingSpellEntity.COLUMN_SOURCE_TAG
    else -> throw IllegalArgumentException("tag name not supported")
}

internal fun List<TagEnum>.toTableFields() =
    this.joinToString { "'$it'" }