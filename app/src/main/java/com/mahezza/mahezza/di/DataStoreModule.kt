package com.mahezza.mahezza.di

import android.content.Context
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext
        context: Context
    ) : MahezzaDataStore = MahezzaDataStore(context)
}