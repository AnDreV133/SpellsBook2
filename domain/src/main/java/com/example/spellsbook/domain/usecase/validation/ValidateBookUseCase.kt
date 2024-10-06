package com.example.spellsbook.domain.usecase.validation

import com.example.spellsbook.domain.model.BookModel

class ValidateBookUseCase {
    fun execute(model: BookModel): ValidationResult {
        ValidateBookNameUseCase().execute(model.name).also {
            if (!it.successful) return it
        }
        // for another validations
        return ValidationResult(successful = true)
    }
}


class ValidateBookNameUseCase {
    private val MAX_SYMBOLS = 25

    fun execute(name: String) =
        if (name.isBlank())
            ValidationResult(
                successful = false,
                errorMessage = "Name can't be empty"
            )
        else if (name.length > MAX_SYMBOLS)
            ValidationResult(
                successful = false,
                errorMessage = "Name can't be more than $MAX_SYMBOLS symbols"
            )
        else if (Regex("[0-9]").containsMatchIn(name))
            ValidationResult(
                successful = false,
                errorMessage = "Name should contain only letters"
            )
        else
            ValidationResult(
                successful = true,
            )
}

