package com.example.spellsbook.app.di

import androidx.compose.ui.text.intl.Locale
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.usecase.ValidateBookUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DataModule::class])
@InstallIn(SingletonComponent::class)
class DomainModule {
    @Provides
    fun provideValidateBookNameUseCase() =
        ValidateBookUseCase()

    @Provides
    fun provideLocale(): LocaleEnum {
        LocaleEnum.entries.forEach { appLocale ->
            Locale.current.language.also { systemLocale ->
                if (appLocale.value == systemLocale) return appLocale
            }
        }
        return LocaleEnum.ENGLISH
    }
}