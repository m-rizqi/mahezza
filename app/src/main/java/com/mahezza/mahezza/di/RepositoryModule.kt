package com.mahezza.mahezza.di

import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.data.repository.MainAuthRepository
import com.mahezza.mahezza.data.repository.MainUserRepository
import com.mahezza.mahezza.data.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindAuthRepository(repository: MainAuthRepository): AuthRepository

    @Binds
    abstract fun bindUserRepository(repository: MainUserRepository): UserRepository
}