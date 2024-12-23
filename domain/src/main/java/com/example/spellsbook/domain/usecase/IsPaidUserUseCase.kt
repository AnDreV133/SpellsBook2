package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.repository.SettingsRepository
import javax.inject.Inject

class IsPaidUserUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend fun execute(): Boolean {
        return settingsRepository.isPaidUser()
    }
}