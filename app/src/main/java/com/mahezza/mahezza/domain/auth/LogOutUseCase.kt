package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.domain.Result

interface LogOutUseCase {
    suspend operator fun invoke() : Result<Boolean>
}