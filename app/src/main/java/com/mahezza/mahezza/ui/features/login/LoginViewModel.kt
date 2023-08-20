package com.mahezza.mahezza.ui.features.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val EMAIL_KEY = "email"
    private val PASSWORD_KEY = "password"

    val email : StateFlow<String> = savedStateHandle.getStateFlow(EMAIL_KEY, "")
    val password : StateFlow<String> = savedStateHandle.getStateFlow(PASSWORD_KEY, "")

    fun setEmail(value : String){
        savedStateHandle[EMAIL_KEY] = value
    }

    fun setPassword(value : String){
        savedStateHandle[PASSWORD_KEY] = value
    }

}