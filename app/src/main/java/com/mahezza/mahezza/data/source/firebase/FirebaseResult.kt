package com.mahezza.mahezza.data.source.firebase

import com.mahezza.mahezza.common.StringResource

data class FirebaseResult<T>(
    val data : T?,
    val isSuccess : Boolean,
    val message : StringResource?
)