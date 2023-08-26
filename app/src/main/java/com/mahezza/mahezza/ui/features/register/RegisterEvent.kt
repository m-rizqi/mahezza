package com.mahezza.mahezza.ui.features.register

import android.content.Intent

sealed class RegisterEvent {
    object OnRegisterClicked : RegisterEvent()
    object OnRegisterWithGoogleClicked : RegisterEvent()
    class OnGoogleSignInResult(val intent: Intent?) : RegisterEvent()
    object OnCreateProfileScreenStarted : RegisterEvent()
    object OnGeneralMessageShowed : RegisterEvent()
    object OnGoogleSignInStarted: RegisterEvent()

}