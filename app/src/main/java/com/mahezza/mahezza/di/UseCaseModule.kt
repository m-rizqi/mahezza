package com.mahezza.mahezza.di

import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCase
import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindRegisterUseCaseModule(useCase: RegisterWithEmailAndPasswordUseCaseImpl) : RegisterWithEmailAndPasswordUseCase
}