package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagNameEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetShortSpellsByBookIdUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val locale: LocaleEnum
) {
    fun execute(
        id: Long,
        filter: Map<TagNameEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME
    ): Flow<List<SpellShortModel>> {
        return spellRepository.getSpellsShortByBookId(id, locale, filter, sorter)
    }
}