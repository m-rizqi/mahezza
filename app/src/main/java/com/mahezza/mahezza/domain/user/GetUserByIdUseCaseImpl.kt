package com.mahezza.mahezza.domain.user

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.data.repository.UserRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.Result
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUserByIdUseCaseImpl @Inject constructor(
    private val dataStore: MahezzaDataStore,
    private val userRepository: UserRepository
) : GetUserByIdUseCase {
    override suspend fun invoke(id: String): Result<User> {
        val userId = if (id.isNotBlank()) id else dataStore.firebaseUserIdPreference.first()
        if (userId.isNullOrBlank()) {
            return Result.Fail(StringResource.StringResWithParams(R.string.user_id_cant_null_or_blank))
        }
        val result = userRepository.getUserById(userId)
        when(result){
            is com.mahezza.mahezza.data.Result.Fail -> return Result.Fail(result.message)
            is com.mahezza.mahezza.data.Result.Success -> {
                val user = result.data
                    ?: return Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))
                return Result.Success(user)
            }
        }
    }
}