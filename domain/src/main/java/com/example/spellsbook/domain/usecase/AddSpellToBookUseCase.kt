package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.repository.BooksWithSpellsRepository
import javax.inject.Inject

class AddSpellToBookUseCase @Inject constructor(
    private val booksWithSpellsRepository: BooksWithSpellsRepository
){
    suspend fun execute(bookId: Long, spellUuid: String) {
        booksWithSpellsRepository.addSpellToBook(bookId, spellUuid)
    }
}