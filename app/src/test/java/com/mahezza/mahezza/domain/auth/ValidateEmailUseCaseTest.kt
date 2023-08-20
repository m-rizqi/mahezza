package com.mahezza.mahezza.domain.auth

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import com.mahezza.mahezza.domain.Result
import org.junit.Assert.assertTrue


class ValidateEmailUseCaseTest{

    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    @Before
    fun setUp() {
        validateEmailUseCase = ValidateEmailUseCase()
    }

    @Test
    fun `valid email returns success result`() {
        val validEmail = "test@example.com"
        val result = validateEmailUseCase(validEmail)
        assertTrue(result is Result.Success)
    }

    @Test
    fun `invalid email returns fail result`() {
        val invalidEmail = "invalidemail"
        val result = validateEmailUseCase(invalidEmail)
        assertTrue(result is Result.Fail)
    }
}