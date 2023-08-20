package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import java.util.regex.Pattern
import com.mahezza.mahezza.domain.Result

const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[.@#$%^&+=]).{8,}$"

class ValidatePasswordUseCase {
    operator fun invoke(password : String): Result<String> {
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        val isMatch = matcher.matches()
        return if (isMatch){
            Result.Success(password)
        }else{
            Result.Fail(
                StringResource.StringResWithParams(R.string.min_8_chars_letter_and_number)
            )
        }
    }
}