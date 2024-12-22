package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.mapper.mapToDetailModel
import com.example.spellsbook.data.mapper.mapToShortModel
import com.example.spellsbook.data.store.dao.SpellDao
import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository

class SpellRepositoryImpl(
    private val spellDao: SpellDao,
) : SpellRepository {
    override suspend fun getSpellsShort(
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum,
        language: LocaleEnum
    ): List<SpellShortModel> =
        spellDao.getSpellsShort(
            filter,
            sorter,
            language
        ).map { it.mapToShortModel() }

    override suspend fun getSpellDetailByUuid(
        uuid: String,
        language: LocaleEnum
    ): SpellDetailModel =
        spellDao
            .getSpellDetail(uuid, language = language.value)
            .mapToDetailModel()
}