package com.mahezza.mahezza.ui.features.register

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.source.firebase.response.BeginSignInResultResponse

data class RegisterUiState(
    val email : String = "",
    val password : String = "",
    val shouldStartCreateProfileScreen : StartCreateProfileScreen? = null,
    val generalError : StringResource? = null,
    val emailError : StringResource? = null,
    val passwordError : StringResource? = null,
    val isShowLoading : Boolean = false,
    val signInResultResponse: BeginSignInResultResponse? = null,
){
    data class StartCreateProfileScreen(
        val userId : String
    )
}