package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.Result
import javax.inject.Inject

class LoginWithEmailAndPasswordUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: MahezzaDataStore
) : LoginWithEmailAndPasswordUseCase{
    override suspend fun invoke(email: String, password: String): Result<String> {
        val loginResult = authRepository.loginWithEmailAndPassword(email, password)
        when(loginResult){
            is com.mahezza.mahezza.data.Result.Fail -> return Result.Fail(loginResult.message)
            is com.mahezza.mahezza.data.Result.Success -> {
                val firebaseUser = loginResult.data
                    ?: return Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))
                dataStore.saveFirebaseUserIdToPreferencesStore(firebaseUser.uid)
                dataStore.saveLoginToPreferencesStore(true)
                return Result.Success(firebaseUser.uid)
            }
        }
    }

}