package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.data.model.User
import com.mahezza.mahezza.domain.Result

interface LoginWithEmailAndPasswordUseCase {
    suspend operator fun invoke(email: String, password: String): Result<String>
}