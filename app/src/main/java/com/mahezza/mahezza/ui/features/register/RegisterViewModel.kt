package com.mahezza.mahezza.ui.features.register

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.auth.RegisterWithEmailAndPasswordUseCase
import com.mahezza.mahezza.domain.auth.RegisterWithGoogleUseCase
import com.mahezza.mahezza.domain.auth.ValidateEmailUseCase
import com.mahezza.mahezza.domain.auth.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val registerWithEmailAndPasswordUseCase: RegisterWithEmailAndPasswordUseCase,
    private val registerWithGoogleUseCase: RegisterWithGoogleUseCase
) : ViewModel() {

    private val validateEmailUseCase: ValidateEmailUseCase = ValidateEmailUseCase()
    private val validatePasswordUseCase: ValidatePasswordUseCase = ValidatePasswordUseCase()

    private val EMAIL_KEY = "email"
    private val PASSWORD_KEY = "password"

    val email : StateFlow<String> = savedStateHandle.getStateFlow(EMAIL_KEY, "")
    val password : StateFlow<String> = savedStateHandle.getStateFlow(PASSWORD_KEY, "")

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState : StateFlow<RegisterUiState>
        get() = _uiState.asStateFlow()

    fun setEmail(value : String){
        savedStateHandle[EMAIL_KEY] = value
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun setPassword(value : String){
        savedStateHandle[PASSWORD_KEY] = value
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun onEvent(event: RegisterEvent){
        when(event){
            is RegisterEvent.OnRegisterClicked -> registerWithEmailAndPassword()
            is RegisterEvent.OnRegisterWithGoogleClicked -> registerWithGoogle()
            is RegisterEvent.OnCreateProfileScreenStarted -> _uiState.update {
                it.copy(shouldStartCreateProfileScreen = null)
            }
            is RegisterEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalError = null) }
            is RegisterEvent.OnGoogleSignInResult -> {
                signInWithCredential(event.intent)
            }
            RegisterEvent.OnGoogleSignInStarted -> _uiState.update { it.copy(signInResultResponse = null) }
        }
    }

    private fun updateUiStateToLatestStateHandle(){
        _uiState.update {
            it.copy(
                email = email.value,
                password = password.value
            )
        }
    }

    private fun registerWithEmailAndPassword(){
        updateUiStateToLatestStateHandle()
        val latestState = uiState.value
        val emailValidity = validateEmailUseCase(latestState.email)
        val passwordValidity = validatePasswordUseCase(latestState.password)
        if (emailValidity is Result.Fail) {
            _uiState.update {
                it.copy(emailError = emailValidity.message)
            }
        }
        if (passwordValidity is Result.Fail) {
            _uiState.update {
                it.copy(passwordError = passwordValidity.message)
            }
        }
        if (isEmailAndPasswordValid(emailValidity, passwordValidity).not()){
            return
        }
        _uiState.update { it.copy(isShowLoading = true) }
        viewModelScope.launch {
            val result = registerWithEmailAndPasswordUseCase.invoke(latestState.email, latestState.password)
            when(result){
                is Result.Fail -> _uiState.update { it.copy(generalError = result.message) }
                is Result.Success -> _uiState.update { it.copy(shouldStartCreateProfileScreen = RegisterUiState.StartCreateProfileScreen(result.data ?: "")) }
            }
            _uiState.update { it.copy(isShowLoading = false) }
        }
    }

    private fun isEmailAndPasswordValid(emailValidity: Result<String>, passwordValidity: Result<String>): Boolean =
        emailValidity is Result.Success && passwordValidity is Result.Success

    private fun registerWithGoogle(){
        viewModelScope.launch {
            _uiState.update { it.copy(isShowLoading = true) }
            val signInResultResponse = registerWithGoogleUseCase.beginSignInRequest()
            _uiState.update { it.copy(signInResultResponse = signInResultResponse, isShowLoading = false) }
        }
    }

    private fun signInWithCredential(intent: Intent?){
        _uiState.update { it.copy(isShowLoading = true) }
        viewModelScope.launch {
            val result = registerWithGoogleUseCase.signInWithCredential(intent)
            when(result){
                is Result.Fail -> _uiState.update { it.copy(generalError = result.message) }
                is Result.Success -> _uiState.update { it.copy(shouldStartCreateProfileScreen = RegisterUiState.StartCreateProfileScreen(result.data ?: "")) }
            }
            _uiState.update { it.copy(isShowLoading = false) }
        }
    }


}