package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.domain.Result

interface ForgotPasswordUseCase {
    suspend operator fun invoke(email : String): Result<Boolean>
}