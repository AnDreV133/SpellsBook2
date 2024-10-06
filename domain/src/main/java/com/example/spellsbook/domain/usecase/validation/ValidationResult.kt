package com.example.spellsbook.domain.usecase.validation

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)