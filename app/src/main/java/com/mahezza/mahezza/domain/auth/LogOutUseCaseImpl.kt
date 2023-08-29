package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.Result
import javax.inject.Inject

class LogOutUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: MahezzaDataStore
) : LogOutUseCase {
    override suspend fun invoke(): Result<Boolean> {
        val result = authRepository.logOut()
        if (result is com.mahezza.mahezza.data.Result.Fail) return Result.Fail(result.message)

        dataStore.saveLoginToPreferencesStore(false)
        dataStore.saveFirebaseUserIdToPreferencesStore("")

        return Result.Success(data = true)
    }
}