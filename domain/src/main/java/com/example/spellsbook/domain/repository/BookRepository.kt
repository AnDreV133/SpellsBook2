package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.model.BookModel
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun getAll(): Flow<List<BookModel>>

    suspend fun add(bookModel: BookModel): Long
}