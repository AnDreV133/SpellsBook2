package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagNameEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface SpellRepository {
    fun getSpellsShortByBookId(
        bookId: Long,
        locale: LocaleEnum,
        filter: Map<TagNameEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ): Flow<List<SpellShortModel>>
    fun getSpellsShort(locale: LocaleEnum): Flow<List<SpellShortModel>>
    fun getSpellByUuid(uuid: UUID, locale: LocaleEnum): Flow<SpellDetailModel>
}