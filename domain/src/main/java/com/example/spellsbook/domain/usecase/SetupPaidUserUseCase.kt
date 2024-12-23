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
    private val scope = CoroutineScope(Dispatchers.IO)

    fun switch(currPaidUser: Boolean) {
        if (currPaidUser) disable()
        else enable()
    }

    fun enable() {
        scope.launch {
            settingsRepository.enablePaidUser()
        }
    }

    fun disable() {
        scope.launch {
            settingsRepository.disablePaidUser()
        }
    }

    suspend fun isEnable(): Boolean {
        return settingsRepository.isPaidUser()
    }
}