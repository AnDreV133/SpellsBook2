package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.repository.BookRepository
import javax.inject.Inject

class GetAllBooksUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend fun execute(): List<BookModel> {
        return bookRepository.getAll()
    }
}