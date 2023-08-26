package com.mahezza.mahezza.ui.features.login

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse

data class LoginUiState(
    val email : String = "",
    val password : String = "",
    val shouldGoToDashboard : Boolean = false,
    val generalError : StringResource? = null,
    val emailError : StringResource? = null,
    val passwordError : StringResource? = null,
    val isShowLoading : Boolean = false,
    val signInResultResponse: BeginSignInResultResponse? = null,
)