package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.BookModel
import com.example.spellsbook.domain.repository.BookRepository
import javax.inject.Inject

class AddBookUseCase @Inject constructor(
    private val bookRepository: BookRepository
) {
    suspend fun execute(model: BookModel) {
        bookRepository.add(
            model.copy(
                name = model.name.trim()
            )
        )
    }
}