package com.mahezza.mahezza.domain.auth

import org.junit.Assert
import org.junit.Test
import com.mahezza.mahezza.domain.Result

class ValidatePasswordUseCaseTest {

    private val validatePasswordUseCase = ValidatePasswordUseCase()

    @Test
    fun `Password less than 8 chars, fail`(){
        val result = validatePasswordUseCase("q.1234")
        Assert.assertTrue(result is Result.Fail)
    }

    @Test
    fun `Password doesn't contain number, fail`(){
        val result = validatePasswordUseCase("Zxasqw.,d")
        Assert.assertTrue(result is Result.Fail)
    }

    @Test
    fun `Password doesn't contain letter, fail`(){
        val result = validatePasswordUseCase("12345678,.")
        Assert.assertTrue(result is Result.Fail)
    }

    @Test
    fun `Password doesn't contain symbol, fail`(){
        val result = validatePasswordUseCase("Zxasqw1234")
        Assert.assertTrue(result is Result.Fail)
    }

    @Test
    fun `Password is min 8 chars and contains letter, number, and symbol, success`(){
        val result = validatePasswordUseCase("Sdjk123.8,")
        Assert.assertTrue(result is Result.Fail)
    }

}