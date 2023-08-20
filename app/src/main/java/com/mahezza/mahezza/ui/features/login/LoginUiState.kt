package com.mahezza.mahezza.ui.features.login

import com.mahezza.mahezza.common.StringResource

data class LoginUiState(
    val email : String = "",
    val password : String = "",
    val shouldGoToDashboard : Boolean = false,
    val generalError : StringResource? = null,
    val emailError : StringResource? = null,
    val passwordError : StringResource? = null
)