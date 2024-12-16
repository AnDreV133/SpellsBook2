package com.example.spellsbook.domain.repository

interface SettingsRepository {

    suspend fun isPaidUser(): Boolean
    suspend fun disablePaidUser()
    suspend fun enablePaidUser()
}