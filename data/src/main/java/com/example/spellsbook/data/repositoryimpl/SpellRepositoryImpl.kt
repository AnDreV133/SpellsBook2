package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.mapper.mapToShortModel
import com.example.spellsbook.data.store.dao.BookWithSpellsDao
import com.example.spellsbook.data.store.dao.SpellDaoEn
import com.example.spellsbook.data.store.dao.SpellDaoRu
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.SpellRepository

class SpellRepositoryImpl(
    private val spellDaoEn: SpellDaoEn,
    private val spellDaoRu: SpellDaoRu,
    private val bookWithSpellsDao: BookWithSpellsDao
) : SpellRepository {
    override suspend fun getSpellsByBookId(
        bookId: Long,
        locale: LocaleEnum
    ): List<SpellShortModel> =
        bookWithSpellsDao
            .getByBookId(bookId)
            .map { it.spellUuid }
            .let { uuids ->
                defineSpellDao(locale)
                    .getByUuid(uuids)
                    .map { it.mapToShortModel() }
            }

    override suspend fun gatAllSpells(locale: LocaleEnum): List<SpellShortModel> =
        defineSpellDao(locale)
            .getAll()
            .map { it.mapToShortModel() }

    override suspend fun getSpellsByUuid(
        vararg uuids: String,
        locale: LocaleEnum
    ): List<SpellShortModel> =
        uuids.map { uuid ->
            defineSpellDao(locale)
                .getByUuid(uuid)
                .mapToShortModel()
        }

    private fun defineSpellDao(locale: LocaleEnum) =
        when (locale) {
            LocaleEnum.ENGLISH -> spellDaoEn
            LocaleEnum.RUSSIAN -> spellDaoRu
        }
}