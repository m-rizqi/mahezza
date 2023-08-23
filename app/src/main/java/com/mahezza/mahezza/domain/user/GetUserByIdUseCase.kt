package com.mahezza.mahezza.domain.user

import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.domain.Result

interface GetUserByIdUseCase {
    suspend fun invoke(id: String) : Result<User>
}