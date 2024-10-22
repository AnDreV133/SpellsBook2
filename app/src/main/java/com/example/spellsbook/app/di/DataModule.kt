package com.example.spellsbook.app.di

import android.content.Context
import com.example.spellsbook.data.store.AppDatabase
import com.example.spellsbook.data.store.AppDatabaseConnection
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
    @Singleton
    fun provideInitDao(
        appDatabase: AppDatabase
    ) = appDatabase.initDao()
}