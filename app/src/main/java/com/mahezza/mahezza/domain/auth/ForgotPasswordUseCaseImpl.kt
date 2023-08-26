package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.data.repository.AuthRepository
import com.mahezza.mahezza.domain.Result
import javax.inject.Inject

class ForgotPasswordUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
): ForgotPasswordUseCase {
    private val validateEmailUseCase = ValidateEmailUseCase()
    override suspend fun invoke(email: String): Result<Boolean> {
        val emailValidity = validateEmailUseCase(email)
        if (emailValidity is Result.Fail) return Result.Fail(emailValidity.message)

        val result = authRepository.sendPasswordResetEmail(email)
        return when(result){
            is com.mahezza.mahezza.data.Result.Fail -> Result.Fail(result.message)
            is com.mahezza.mahezza.data.Result.Success -> Result.Success(true)
        }
    }
}