package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.mapper.mapToDetailModel
import com.example.spellsbook.data.mapper.mapToShortModel
import com.example.spellsbook.data.store.dao.BookWithSpellsDao
import com.example.spellsbook.data.store.dao.SpellDao
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.enums.SortOptionEnum
import com.example.spellsbook.domain.enums.TagEnum
import com.example.spellsbook.domain.enums.TagNameEnum
import com.example.spellsbook.domain.model.SpellDetailModel
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import java.util.UUID

class SpellRepositoryImpl(
    private val spellDao: SpellDao,
) : SpellRepository {
//    override fun getSpellByUuid(uuid: UUID, locale: LocaleEnum): Flow<SpellDetailModel> {
//        TODO("Not yet implemented")
//    }

    override fun getSpellsShortByBookId(
        bookId: Long,
        locale: LocaleEnum,
        filter: Map<TagNameEnum, List<TagEnum>>,
        sorter: SortOptionEnum
    ): Flow<List<SpellShortModel>> =
        spellDao.getSpellsShort(
            locale,
            bookId,
            filter,
            sorter
        ).transform {
            it.map { it.mapToShortModel() }
        }


    override fun getSpellsShort(
        locale: LocaleEnum
    ): Flow<List<SpellShortModel>> =
        spellDao.getSpellsShort(locale).transform { it.map { it.mapToShortModel() } }

    override fun getSpellByUuid(
        uuid: UUID,
        locale: LocaleEnum
    ): Flow<SpellDetailModel> =
        spellDao.getSpellDetail(uuid, locale).transform { it.mapToDetailModel() }
}