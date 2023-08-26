package com.mahezza.mahezza.ui.features.login

import android.content.Intent

sealed class LoginEvent {
    object OnLoginClicked : LoginEvent()
    object OnLoginWithGoogleClicked : LoginEvent()
    class OnForgotPasswordRequest(val email : String) : LoginEvent()
    object OnDashboardScreenStarted : LoginEvent()
    object OnGeneralErrorShowed : LoginEvent()
    class OnGoogleSignInResult(val intent: Intent?) : LoginEvent()
    object OnGoogleSignInStarted: LoginEvent()
}