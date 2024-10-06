package com.example.spellsbook.domain.repository

import com.example.spellsbook.domain.model.BookModel

interface BookRepository {
    suspend fun getAll(): List<BookModel>

    suspend fun add(bookModel: BookModel): Long
}