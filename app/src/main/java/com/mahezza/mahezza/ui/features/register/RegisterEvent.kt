package com.mahezza.mahezza.ui.features.register

sealed class RegisterEvent {
    object OnRegisterClicked : RegisterEvent()
    object OnRegisterWithGoogleClicked : RegisterEvent()
    object OnCreateProfileScreenStarted : RegisterEvent()
    object OnGeneralMessageShowed : RegisterEvent()
}