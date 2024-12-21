package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.repository.BooksWithSpellsRepository
import javax.inject.Inject

class RemoveSpellFromBookUseCase @Inject constructor(
    private val booksWithSpellsRepository: BooksWithSpellsRepository
) {
    suspend fun execute(bookId: Long, spellUuid: String) {
        booksWithSpellsRepository.removeSpellFromBook(bookId, spellUuid)
    }
}