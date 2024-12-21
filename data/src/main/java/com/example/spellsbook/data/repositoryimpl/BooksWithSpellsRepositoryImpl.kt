package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.mapper.mapToShortModel
import com.example.spellsbook.data.store.dao.BooksWithSpellsDao
import com.example.spellsbook.data.store.dao.SpellDao
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.model.SpellShortModel
import com.example.spellsbook.domain.repository.BooksWithSpellsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BooksWithSpellsRepositoryImpl(
    private val bookWithSpellsDao: BooksWithSpellsDao,
    private val spellDao: SpellDao
) : BooksWithSpellsRepository {
    override suspend fun addSpellToBook(bookId: Long, spellUuid: String): Long {
        return bookWithSpellsDao.insert(bookId, spellUuid)
    }

    override suspend fun removeSpellFromBook(bookId: Long, spellUuid: String): Int {
        return bookWithSpellsDao.delete(bookId, spellUuid)
    }

    override fun getSpellsByBookId(
        bookId: Long,
        language: LocaleEnum
    ): Flow<List<SpellShortModel>> =
        spellDao.getSpellsShortByBookId(bookId, language.value)
            .map { list ->
                list.map { it.mapToShortModel() }
            }

    override suspend fun getUuidsByBookId(bookId: Long): List<String> =
        bookWithSpellsDao.getByBookId(bookId)
}