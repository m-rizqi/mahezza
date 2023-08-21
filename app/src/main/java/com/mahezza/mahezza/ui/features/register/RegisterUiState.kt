package com.mahezza.mahezza.ui.features.register

import com.mahezza.mahezza.common.StringResource

data class RegisterUiState(
    val email : String = "",
    val password : String = "",
    val shouldStartCreateProfileScreen : Boolean = false,
    val generalError : StringResource? = null,
    val emailError : StringResource? = null,
    val passwordError : StringResource? = null,
    val isShowLoading : Boolean = false,
)