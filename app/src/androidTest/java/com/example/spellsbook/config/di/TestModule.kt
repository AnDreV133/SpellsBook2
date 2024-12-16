package com.example.spellsbook.config.di

import com.example.spellsbook.app.di.DataModule
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.domain.LocaleEnum
import com.example.spellsbook.domain.repository.SpellRepository
import com.example.spellsbook.domain.repository.TagRepository
import com.example.spellsbook.domain.usecase.GetSpellsWithFilterAndSorterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DataModule::class, UseCaseModule::class])
@InstallIn(SingletonComponent::class)
class TestModule {
    @Provides
    fun provideGetSpellsWithFilterAndSorterUseCase(
        spellRepository: SpellRepository,
        tagRepository: TagRepository,
        locale: LocaleEnum
    ): GetSpellsWithFilterAndSorterUseCase {
        return GetSpellsWithFilterAndSorterUseCase(
            spellRepository = spellRepository,
            tagRepository = tagRepository,
            locale = locale
        )
    }

    @Provides
    fun provideLevelDao(
        db: AppDatabase
    ) = db.levelDao()

    @Provides
    fun provideSchoolDao(
        db: AppDatabase
    ) = db.schoolDao()
}