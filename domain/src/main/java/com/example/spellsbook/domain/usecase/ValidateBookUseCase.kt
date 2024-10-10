package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.model.BookModel

class ValidateBookUseCase {
    private val MAX_SYMBOLS = 25

    fun execute(model: BookModel): ValidationResult {
        model.name.checkBookName().also {
            if (!it.successful) return it
        }
        // for another validations
        return ValidationResult(successful = true)
    }

    private fun String.checkBookName(): ValidationResult =
        if (this.isBlank())
            ValidationResult(
                successful = false,
                errorMessage = "Name can't be empty"
            )
        else if (this.length > MAX_SYMBOLS)
            ValidationResult(
                successful = false,
                errorMessage = "Name can't be more than $MAX_SYMBOLS symbols"
            )
        else if (Regex("[0-9]").containsMatchIn(this))
            ValidationResult(
                successful = false,
                errorMessage = "Name should contain only letters"
            )
        else
            ValidationResult(
                successful = true,
            )
}

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)

