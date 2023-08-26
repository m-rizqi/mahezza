package com.mahezza.mahezza.data.source.firebase.response

import com.google.firebase.auth.FirebaseUser
import com.mahezza.mahezza.common.StringResource

data class SignInRegisterResponse(
    val isSuccess : Boolean,
    val firebaseUser: FirebaseUser?,
    val message : StringResource?
)