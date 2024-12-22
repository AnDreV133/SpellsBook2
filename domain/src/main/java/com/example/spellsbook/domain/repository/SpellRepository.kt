package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel

interface SpellRepository {
    suspend fun getSpellDetailByUuid(
        uuid: String,
        language: LocaleEnum
    ): SpellDetailModel

    suspend fun getSpellsShort(
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum,
        language: LocaleEnum
    ): List<SpellShortModel>
}