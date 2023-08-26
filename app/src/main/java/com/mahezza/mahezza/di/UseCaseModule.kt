package com.mahezza.mahezza.di

import com.mahezza.mahezza.domain.auth.LoginWithEmailAndPasswordUseCase
import com.mahezza.mahezza.domain.auth.LoginWithEmailAndPasswordUseCaseImpl
import com.mahezza.mahezza.domain.auth.LoginWithGoogleUseCase
import com.mahezza.mahezza.domain.auth.LoginWithGoogleUseCaseImpl
import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCase
import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCaseImpl
import com.mahezza.mahezza.domain.auth.RegisterWithGoogleUseCase
import com.mahezza.mahezza.domain.auth.RegisterWithGoogleUseCaseImpl
import com.mahezza.mahezza.domain.children.InsertChildUseCase
import com.mahezza.mahezza.domain.children.InsertChildUseCaseImpl
import com.mahezza.mahezza.domain.puzzle.RedeemPuzzleUseCase
import com.mahezza.mahezza.domain.puzzle.RedeemPuzzleUseCaseImpl
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
    abstract fun bindLoginWithEmailAndPasswordUseCase(useCase: LoginWithEmailAndPasswordUseCaseImpl) : LoginWithEmailAndPasswordUseCase

    @Binds
    abstract fun bindLoginWithGoogleUseCase(useCase: LoginWithGoogleUseCaseImpl) : LoginWithGoogleUseCase

    @Binds
    abstract fun bindRegisterUseCase(useCase: RegisterWithEmailAndPasswordUseCaseImpl) : RegisterWithEmailAndPasswordUseCase

    @Binds
    abstract fun bindRegisterWithGoogleUseCase(useCase: RegisterWithGoogleUseCaseImpl) : RegisterWithGoogleUseCase

    @Binds
    abstract fun bindGetUserByIdUseCase(useCase: GetUserByIdUseCaseImpl) : GetUserByIdUseCase

    @Binds
    abstract fun bindCreateProfileUseCase(useCase: CreateProfileUseCaseImpl) : CreateProfileUseCase

    @Binds
    abstract fun bindInsertChildProfileUseCase(useCase: InsertChildUseCaseImpl) : InsertChildUseCase
    @Binds
    abstract fun bindRedeemPuzzleUseCase(useCase: RedeemPuzzleUseCaseImpl) : RedeemPuzzleUseCase
}