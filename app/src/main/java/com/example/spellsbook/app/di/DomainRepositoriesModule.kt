package com.example.spellsbook.app.di

import com.example.spellsbook.data.repositoryimpl.BookRepositoryImpl
import com.example.spellsbook.data.repositoryimpl.BooksWithSpellsRepositoryImpl
import com.example.spellsbook.data.repositoryimpl.SettingsRepositoryImpl
import com.example.spellsbook.data.repositoryimpl.SpellByAuthorRepositoryImpl
import com.example.spellsbook.data.repositoryimpl.SpellRepositoryImpl
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.data.store.preferences.AppSharedPreferences
import com.example.spellsbook.domain.repository.BookRepository
import com.example.spellsbook.domain.repository.BooksWithSpellsRepository
import com.example.spellsbook.domain.repository.SettingsRepository
import com.example.spellsbook.domain.repository.SpellByAuthorRepository
import com.example.spellsbook.domain.repository.SpellRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [DataModule::class])
@InstallIn(SingletonComponent::class)
class DomainRepositoriesModule {
    @Provides
    fun provideSpellRepository(db: AppDatabase): SpellRepository =
        SpellRepositoryImpl(
            spellDao = db.spellDao()
        )

    @Provides
    fun provideBookRepository(db: AppDatabase): BookRepository =
        BookRepositoryImpl(
            bookDao = db.bookDao()
        )

    @Provides
    fun provideSettingsRepository(sp: AppSharedPreferences): SettingsRepository =
        SettingsRepositoryImpl(sp)

    @Provides
    fun provideBooksWithSpellsRepository(db: AppDatabase): BooksWithSpellsRepository =
        BooksWithSpellsRepositoryImpl(
            bookWithSpellsDao = db.bookWithSpellsDao(),
            spellDao = db.spellDao()
        )

    @Provides
    fun provideSpellByAuthorRepository(db: AppDatabase): SpellByAuthorRepository =
        SpellByAuthorRepositoryImpl(
            spellByAuthorDao = db.spellByAuthorDao(),
            spellDao = db.spellDao()
        )
}