package com.mahezza.mahezza.ui.features.login

sealed class LoginEvent {
    object OnLoginClicked : LoginEvent()
    object OnLoginWithGoogleClicked : LoginEvent()
    object OnForgotPasswordClicked : LoginEvent()
}