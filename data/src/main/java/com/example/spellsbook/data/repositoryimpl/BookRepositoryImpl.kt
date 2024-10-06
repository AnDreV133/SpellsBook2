package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.mapper.mapToEntity
import com.example.spellsbook.data.mapper.mapToModel
import com.example.spellsbook.data.store.dao.BookDao
import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.repository.BookRepository

class BookRepositoryImpl(
    private val bookDao: BookDao,
) : BookRepository {
    override suspend fun getAll(): List<BookModel> {
        return bookDao.getAll().map { it.mapToModel() }
    }

    override suspend fun add(bookModel: BookModel): Long {
        return bookDao.insert(bookModel.mapToEntity())
    }
}