package com.example.spellsbook.app.di

import android.content.Context
import com.example.spellsbook.data.repositoryimpl.BookRepositoryImpl
import com.example.spellsbook.data.repositoryimpl.SpellRepositoryImpl
import com.example.spellsbook.data.repositoryimpl.TagRepositoryImpl
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.data.store.AppDatabaseConnection
import com.example.spellsbook.domain.repository.BookRepository
import com.example.spellsbook.domain.repository.SpellRepository
import com.example.spellsbook.domain.repository.TagRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
//    @Provides
//    @Singleton
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ) = AppDatabaseConnection.instance(appContext)

    @Provides
    @Singleton
    fun provideTestAppDatabase(
        @ApplicationContext appContext: Context
    ) = AppDatabaseConnection.reinstantiation(appContext)

    @Provides
    fun provideBookRepository(db: AppDatabase): BookRepository {
        return BookRepositoryImpl(
            db.bookDao()
        )
    }

    @Provides
    fun provideTagRepository(db: AppDatabase): TagRepository {
        return TagRepositoryImpl(
            levelDao = db.levelDao(),
            schoolDao = db.schoolDao()
        )
    }

    @Provides
    fun provideSpellRepository(db: AppDatabase): SpellRepository {
        return SpellRepositoryImpl(
            spellDaoEn = db.spellDaoEn(),
            spellDaoRu = db.spellDaoRu(),
            bookWithSpellsDao = db.bookWithSpellsDao()
        )
    }

    // todo for repositories
}