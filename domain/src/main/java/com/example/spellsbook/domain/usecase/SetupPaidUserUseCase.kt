package com.example.spellsbook.domain.usecase

import com.example.spellsbook.domain.repository.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

class SetupPaidUserUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend fun enable() {
        settingsRepository.enablePaidUser()
    }

    suspend fun disable() {
        settingsRepository.disablePaidUser()
    }
}