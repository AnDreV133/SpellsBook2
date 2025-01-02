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
        "and t1.${SpellEntity.COLUMN_LANGUAGE} in ('${language.value}', '${LocaleEnum.DEFAULT.value}')"

internal fun beginCondition() =
    " where 1=1 "


internal fun filterCondition(
    filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap()
) = StringBuilder().apply {
    filter.forEach { entry ->
        if (entry.value.isNotEmpty())
            append(" and ${entry.key.toColumnName()} in (${entry.value.toTableFields()}) ")
    }
}.toString()

internal fun searchCondition(
    word: String = ""
) = " and ${SpellEntity.COLUMN_NAME} like '%$word%' "

internal fun sortQuery(
    sorter: SortOptionEnum = SortOptionEnum.BY_NAME
) = StringBuilder().apply {
    append(" order by ${SpellEntity.COLUMN_NAME} asc ")
    when (sorter) { // fixme: incorrect sort query
        SortOptionEnum.BY_NAME -> Unit
        SortOptionEnum.BY_LEVEL -> append(", ${TaggingSpellEntity.COLUMN_LEVEL_TAG} asc ")
    }
}