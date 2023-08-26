package com.mahezza.mahezza.domain.auth

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import java.util.regex.Pattern
import com.mahezza.mahezza.domain.Result

const val EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

class ValidateEmailUseCase {
    operator fun invoke(email: String): Result<String> {
        val pattern = Pattern.compile(EMAIL_PATTERN)
        val matcher = pattern.matcher(email)
        val isMatch = matcher.matches()
        return if (isMatch) {
            Result.Success(email)
        } else {
            Result.Fail(
                StringResource.StringResWithParams(R.string.invalid_email_format)
            )
        }
    }
}
