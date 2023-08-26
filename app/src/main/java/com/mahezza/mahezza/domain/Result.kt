package com.mahezza.mahezza.domain

import com.mahezza.mahezza.common.StringResource

sealed class Result<T>(val data : T?, val message : StringResource?) {
    class Success<T>(data : T) : Result<T>(data, null)
    class Fail<T>(message : StringResource?) : Result<T>(null, message)
}