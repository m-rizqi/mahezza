package com.mahezza.mahezza.domain.auth

import android.content.Intent
import com.google.firebase.auth.FirebaseUser
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.data.repository.UserRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse
import com.mahezza.mahezza.domain.Result
import javax.inject.Inject

class RegisterWithGoogleUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val dataStore: MahezzaDataStore
) : RegisterWithGoogleUseCase{

    private fun getSimpleUserProfileFromFirebaseUser(firebaseUser: FirebaseUser): User {
        return User(
            id = firebaseUser.uid,
            name = firebaseUser.displayName ?: "",
            photoUrl = firebaseUser.photoUrl.toString(),
            job = "", relationshipWithChildren = "", timeSpendWithChildren = ""
        )
    }

    private suspend fun insertNewUser(user: User): Result<String>{
        val insertResult = userRepository.insertUser(user)
        when(insertResult){
            is com.mahezza.mahezza.data.Result.Success -> {
                if (insertResult.data == null) return Result.Fail(
                    StringResource.StringResWithParams(
                        R.string.problem_occur_try_again))
                dataStore.saveLoginToPreferencesStore(true)
                return Result.Success(insertResult.data)
            }
            is com.mahezza.mahezza.data.Result.Fail -> return Result.Fail(insertResult.message)
        }
    }

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
                val user = getSimpleUserProfileFromFirebaseUser(firebaseUser)
                return insertNewUser(user)
            }
        }
    }
}