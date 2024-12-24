package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    suspend fun get(bookId: Long): BookModel
    fun getAll(): Flow<List<BookModel>>
    suspend fun add(bookModel: BookModel): Long
    suspend fun remove(id: Long): Int
}