package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.domain.Result

interface RegisterWithEmailAndPasswordUseCase {
    suspend fun invoke(email: String, password: String): Result<String>
}