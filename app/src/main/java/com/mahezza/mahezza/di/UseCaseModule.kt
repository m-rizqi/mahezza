package com.mahezza.mahezza.di

import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCase
import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCaseImpl
import com.mahezza.mahezza.domain.auth.RegisterWithGoogleUseCase
import com.mahezza.mahezza.domain.auth.RegisterWithGoogleUseCaseImpl
import com.mahezza.mahezza.domain.user.CreateProfileUseCase
import com.mahezza.mahezza.domain.user.CreateProfileUseCaseImpl
import com.mahezza.mahezza.domain.user.GetUserByIdUseCase
import com.mahezza.mahezza.domain.user.GetUserByIdUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class UseCaseModule {
    @Binds
    abstract fun bindRegisterUseCase(useCase: RegisterWithEmailAndPasswordUseCaseImpl) : RegisterWithEmailAndPasswordUseCase

    @Binds
    abstract fun bindRegisterWithGoogleUseCase(useCase: RegisterWithGoogleUseCaseImpl) : RegisterWithGoogleUseCase

    @Binds
    abstract fun bindGetUserByIdUseCase(useCase: GetUserByIdUseCaseImpl) : GetUserByIdUseCase

    @Binds
    abstract fun bindCreateProfileUseCase(useCase: CreateProfileUseCaseImpl) : CreateProfileUseCase
}