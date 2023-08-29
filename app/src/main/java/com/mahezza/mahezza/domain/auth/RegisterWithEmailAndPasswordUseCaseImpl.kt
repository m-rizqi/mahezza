package com.mahezza.mahezza.domain.auth

import com.google.firebase.auth.FirebaseUser
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.data.repository.UserRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.Result
import javax.inject.Inject

class RegisterWithEmailAndPasswordUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val dataStore: MahezzaDataStore
) : RegisterWithEmailAndPasswordUseCase{
    override suspend fun invoke(email: String, password: String): Result<String> {
        val registerResult = authRepository.registerWithEmailAndPassword(email, password)
        when(registerResult){
            is com.mahezza.mahezza.data.Result.Fail -> return Result.Fail(registerResult.message)
            is com.mahezza.mahezza.data.Result.Success -> {
                val firebaseUser = registerResult.data
                    ?: return Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))
                dataStore.saveFirebaseUserIdToPreferencesStore(firebaseUser.uid)
                dataStore.saveLoginToPreferencesStore(true)
                val user = getSimpleUserProfileFromFirebaseUser(firebaseUser)
                return insertNewUser(user)
            }
        }
    }

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
                if (insertResult.data == null) return Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))
                dataStore.saveLoginToPreferencesStore(true)
                return Result.Success(insertResult.data)
            }
            is com.mahezza.mahezza.data.Result.Fail -> return Result.Fail(insertResult.message)
        }
    }

}