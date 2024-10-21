package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.mapper.mapToDetailModel
import com.example.spellsbook.data.mapper.mapToShortModel
import com.example.spellsbook.data.store.dao.SpellDao
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagIdentifierEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class SpellRepositoryImpl(
    private val spellDao: SpellDao,
) : SpellRepository {
//    override fun getSpellByUuid(uuid: UUID, locale: LocaleEnum): Flow<SpellDetailModel> {
//        TODO("Not yet implemented")
//    }

    override suspend fun getSpellsShortByBookId(
        bookId: Long,
        locale: LocaleEnum,
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ): List<SpellShortModel> =
        spellDao.getSpellsShortByBookId(
            bookId,
            locale,
            filter,
            sorter
        ).map {
            it.mapToShortModel()
        }


    override suspend fun getSpellsShort(
        locale: LocaleEnum,
        filter: Map<TagIdentifierEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ): List<SpellShortModel> =
        spellDao.getSpellsShort(
            locale,
            filter,
            sorter
        ).map { it.mapToShortModel() }

    override fun getSpellByUuid(
        uuid: String,
        locale: LocaleEnum
    ): Flow<SpellDetailModel> =
        spellDao.getSpellDetail(uuid, locale).map { it.mapToDetailModel() }
}