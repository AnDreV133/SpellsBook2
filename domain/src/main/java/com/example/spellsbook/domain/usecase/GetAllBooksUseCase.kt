package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    fun execute(): Flow<List<BookModel>> {
        return bookRepository.getAll()
    }
}