package com.mahezza.mahezza.di

import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.data.repository.ChildrenRepository
import com.mahezza.mahezza.data.repository.CourseRepository
import com.mahezza.mahezza.data.repository.GameRepository
import com.mahezza.mahezza.data.repository.MainAuthRepository
import com.mahezza.mahezza.data.repository.MainChildrenRepository
import com.mahezza.mahezza.data.repository.MainCourseRepository
import com.mahezza.mahezza.data.repository.MainGameRepository
import com.mahezza.mahezza.data.repository.MainPuzzleRepository
import com.mahezza.mahezza.data.repository.MainUserRepository
import com.mahezza.mahezza.data.repository.PuzzleRepository
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

    @Binds
    abstract fun bindChildrenRepository(repository: MainChildrenRepository): ChildrenRepository

    @Binds
    abstract fun bindPuzzleRepository(repository: MainPuzzleRepository): PuzzleRepository

    @Binds
    abstract fun bindGameRepository(repository : MainGameRepository) : GameRepository

    @Binds
    abstract fun bindCourseRepository(repository: MainCourseRepository) : CourseRepository
}