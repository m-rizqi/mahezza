package com.mahezza.mahezza.domain.auth

import android.content.Intent
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse
import com.mahezza.mahezza.domain.Result
import javax.inject.Inject

class LoginWithGoogleUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStore: MahezzaDataStore
) : LoginWithGoogleUseCase{

    override suspend fun beginSignInRequest(): BeginSignInResultResponse {
        return authRepository.beginSignInRequest()
    }

    override suspend fun signInWithCredential(intent: Intent?): Result<String> {
        val signInResult = authRepository.signInWithCredential(intent)
        when(signInResult){
            is com.mahezza.mahezza.data.Result.Fail -> return Result.Fail(signInResult.message)
            is com.mahezza.mahezza.data.Result.Success -> {
                val firebaseUser = signInResult.data
                    ?: return Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))
                dataStore.saveFirebaseUserIdToPreferencesStore(firebaseUser.uid)
                return Result.Success(firebaseUser.uid)
            }
        }
    }
}