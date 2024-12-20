package com.example.spellsbook.data.repositoryimpl

import com.example.spellsbook.data.store.preferences.AppSharedPreferences
import com.example.spellsbook.data.store.preferences.get
import com.example.spellsbook.data.store.preferences.set
import com.example.spellsbook.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private val sp: AppSharedPreferences
) : SettingsRepository {
    override suspend fun enablePaidUser() {
        sp.set(SP_PAID_USER, true)
    }

    override suspend fun disablePaidUser() {
        sp.set(SP_PAID_USER, false)
    }

    override suspend fun isPaidUser(): Boolean {
        return sp.get(SP_PAID_USER)!!
    }

    companion object {
        private const val SP_PAID_USER = "SP_PAID_USER"
    }
}