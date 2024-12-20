package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.model.SpellShortModel
import kotlinx.coroutines.flow.Flow

interface BooksWithSpellsRepository {
    suspend fun addSpellToBook(bookId: Long, spellUuid: String): Long

    suspend fun removeSpellFromBook(bookId: Long, spellUuid: String): Int

    fun getSpellsByBookId(bookId: Long, language: String): Flow<List<SpellShortModel>>
}