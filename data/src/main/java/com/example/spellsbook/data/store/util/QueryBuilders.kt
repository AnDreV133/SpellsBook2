package com.example.spellsbook.data.store.util

import com.example.spellsbook.data.store.entity.SpellEntity
import com.example.spellsbook.data.store.entity.TaggingSpellEntity
import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum

// todo add catching 'default' language


internal fun getSpellsWithTagsShortQuery(
    language: LocaleEnum
) = "select * from ${TaggingSpellEntity.TABLE_NAME} as t0 " +
        "inner join ${SpellEntity.TABLE_NAME} as t1 " +
        "on t0.${SpellEntity.COLUMN_UUID}=t1.${TaggingSpellEntity.COLUMN_UUID} " +
        "and t1.${SpellEntity.COLUMN_LANGUAGE} in ('${language.value}', ${LocaleEnum.DEFAULT})"

internal fun filterSuffixQuery(
    filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
    sorter: SortOptionEnum = SortOptionEnum.BY_NAME
) = StringBuilder().apply {
    // begin condition
    append(" where 1=1 ")
    // set filters
    filter.forEach { entry ->
        if (entry.value.isNotEmpty())
            append("and ${entry.key.toColumnName()} in (${entry.value.toTableFields()}) ")
    }

    // set sorter
    when (sorter) { // fixme: incorrect sort query
        SortOptionEnum.BY_NAME -> Unit

        SortOptionEnum.BY_LEVEL ->
            append(", ${TaggingSpellEntity.COLUMN_LEVEL_TAG} asc ")

        else -> throw IllegalArgumentException("sort option not supported")
    }
    append("order by ${SpellEntity.COLUMN_NAME} asc")
}.toString()

