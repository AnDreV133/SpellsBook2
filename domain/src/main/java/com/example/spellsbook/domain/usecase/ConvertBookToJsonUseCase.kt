package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.enums.LocaleEnum
import com.example.spellsbook.domain.repository.BookRepository
import com.example.spellsbook.domain.repository.SpellRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream
import javax.inject.Inject

class ConvertBookToJsonUseCase @Inject constructor(
    private val spellRepository: SpellRepository,
    private val bookRepository: BookRepository,
    private val localeEnum: LocaleEnum
) {
    suspend fun execute(bookId: Long): Result<Pair<String, InputStream>> {
        val book = bookRepository.get(bookId)
        val spellsJson = spellRepository.getSpellsJsonByBook(bookId, localeEnum)

        return withContext(Dispatchers.IO) {
            try {
                Result.success(
                    Pair(
                        "book_${book.name}.json",
                        JSONObject().apply {
                            put("name", book.name)
                            put("spells", JSONArray(spellsJson))
                        }
                            .toString()
                            .byteInputStream()
                    )
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}