package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import javax.inject.Inject

class GetSpellsWithFilterAndSorterUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val locale: LocaleEnum
) {
    suspend fun execute(
        filter: Map<TagIdentifierEnum, List<TagEnum>> = emptyMap(),
        sorter: SortOptionEnum = SortOptionEnum.BY_NAME,
        searchQuery: String = ""
    ): List<SpellShortModel> =
        spellRepository.getSpellsShort(
            filter = filter,
            sorter = sorter,
            searchQuery = searchQuery,
            language = locale
        )
}