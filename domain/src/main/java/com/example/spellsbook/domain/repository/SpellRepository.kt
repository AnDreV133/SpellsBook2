package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import kotlinx.coroutines.flow.Flow

interface SpellRepository {
    suspend fun getSpellsShortByBookId(
        bookId: Long,
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ): List<SpellShortModel>
    fun getSpellByUuid(uuid: String): Flow<SpellDetailModel>
    suspend fun getSpellsShort(
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum,
        language: LocaleEnum
    ): List<SpellShortModel>
}