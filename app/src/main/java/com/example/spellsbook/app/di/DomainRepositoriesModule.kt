package com.example.spellsbook.app.di

import com.example.spellsbook.data.repositoryimpl.BookRepositoryImpl
import com.example.spellsbook.data.repositoryimpl.SpellRepositoryImpl
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.domain.repository.BookRepository
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
}