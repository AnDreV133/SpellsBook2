package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpellsWithFilterAndSorterUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val locale: LocaleEnum
) {
    suspend fun execute(
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
    ): List<SpellShortModel> =
        spellRepository.getSpellsShort(
            filter = filter,
            sorter = sorter,
            locale = locale,
        )
}