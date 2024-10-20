package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIndentifierEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpellsWithFilterAndSorterUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val locale: LocaleEnum
) {
//    val filter: MutableMap<TagNameEnum, List<TagEnum>> = mutableMapOf()
//    var sorter: SortOptionEnum = SortOptionEnum.BY_NAME
//
//    fun updateFilter(tags: Pair<TagNameEnum, List<TagEnum>>) {
//        filter[tags.first] = tags.second
//    }
//
//    fun getTugs(tagName: TagNameEnum): List<TagEnum> {
//        return filter[tagName] ?: emptyList()
//    }

    fun execute(
        filter: Map<TagIndentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum,
    ): Flow<List<SpellShortModel>> =
        spellRepository.getSpellsShort(
            filter = filter,
            sorter = sorter,
            locale = locale,
        )
}