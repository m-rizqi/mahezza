package com.mahezza.mahezza.di

import com.mahezza.mahezza.domain.auth.ForgotPasswordUseCase
import com.mahezza.mahezza.domain.auth.ForgotPasswordUseCaseImpl
import com.mahezza.mahezza.domain.auth.LogOutUseCase
import com.mahezza.mahezza.domain.auth.LogOutUseCaseImpl
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
import com.mahezza.mahezza.domain.common.DownloadTwibbonUseCase
import com.mahezza.mahezza.domain.common.DownloadTwibbonUseCaseImpl
import com.mahezza.mahezza.domain.game.ResumeGameUseCase
import com.mahezza.mahezza.domain.game.ResumeGameUseCaseImpl
import com.mahezza.mahezza.domain.game.SaveGameUseCase
import com.mahezza.mahezza.domain.game.SaveGameUseCaseImpl
import com.mahezza.mahezza.domain.puzzle.GetRedeemedPuzzleUseCase
import com.mahezza.mahezza.domain.puzzle.GetRedeemedPuzzleUseCaseImpl
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
    abstract fun bindLogOutUseCase(useCase : LogOutUseCaseImpl) : LogOutUseCase

    @Binds
    abstract fun bindForgotPasswordUseCase(useCase: ForgotPasswordUseCaseImpl) : ForgotPasswordUseCase

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

    @Binds
    abstract fun bindGetRedeemedPuzzleUseCase(useCase: GetRedeemedPuzzleUseCaseImpl) : GetRedeemedPuzzleUseCase

    @Binds
    abstract fun bindSaveGameUseCase(useCase : SaveGameUseCaseImpl) : SaveGameUseCase

    @Binds
    abstract fun bindDownloadTwibbonUseCase(useCase : DownloadTwibbonUseCaseImpl) : DownloadTwibbonUseCase

    @Binds
    abstract fun bindResumeGameUseCase(useCase: ResumeGameUseCaseImpl) : ResumeGameUseCase
}