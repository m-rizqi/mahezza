package com.mahezza.mahezza.ui.features.login

import android.content.Intent
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.auth.LoginWithEmailAndPasswordUseCase
import com.mahezza.mahezza.domain.auth.LoginWithGoogleUseCase
import com.mahezza.mahezza.domain.auth.ValidateEmailUseCase
import com.mahezza.mahezza.domain.auth.ValidatePasswordUseCase
import com.mahezza.mahezza.ui.features.register.RegisterUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val loginWithEmailAndPasswordUseCase: LoginWithEmailAndPasswordUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val validateEmailUseCase: ValidateEmailUseCase = ValidateEmailUseCase()
    private val validatePasswordUseCase: ValidatePasswordUseCase = ValidatePasswordUseCase()

    private val EMAIL_KEY = "email"
    private val PASSWORD_KEY = "password"

    val email : StateFlow<String> = savedStateHandle.getStateFlow(EMAIL_KEY, "")
    val password : StateFlow<String> = savedStateHandle.getStateFlow(PASSWORD_KEY, "")

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState : StateFlow<LoginUiState>
        get() = _loginUiState.asStateFlow()

    fun setEmail(value : String){
        savedStateHandle[EMAIL_KEY] = value
        _loginUiState.update { it.copy(email = value) }
    }

    fun setPassword(value : String){
        savedStateHandle[PASSWORD_KEY] = value
        _loginUiState.update { it.copy(password = value) }
    }

    fun onEvent(event: LoginEvent){
        when(event){
            LoginEvent.OnLoginClicked -> loginWithEmailAndPassword()
            LoginEvent.OnLoginWithGoogleClicked -> loginWithGoogle()
            LoginEvent.OnForgotPasswordClicked -> forgotPassword()
            LoginEvent.OnDashboardScreenStarted -> _loginUiState.update { it.copy(shouldGoToDashboard = false) }
            LoginEvent.OnGeneralErrorShowed -> _loginUiState.update { it.copy(generalError = null) }
            is LoginEvent.OnGoogleSignInResult -> signInWithCredential(event.intent)
            LoginEvent.OnGoogleSignInStarted -> _loginUiState.update { it.copy(signInResultResponse = null) }
        }
    }

    private fun updateUiStateToLatestStateHandle(){
        _loginUiState.update {
            it.copy(
                email = email.value,
                password = password.value
            )
        }
    }

    private fun loginWithEmailAndPassword(){
        updateUiStateToLatestStateHandle()
        val latestState = loginUiState.value
        val emailValidity = validateEmailUseCase(latestState.email)
        val passwordValidity = validatePasswordUseCase(latestState.password)
        if (emailValidity is Result.Fail) {
            _loginUiState.update {
                it.copy(emailError = emailValidity.message)
            }
        }
        if (passwordValidity is Result.Fail) {
            _loginUiState.update {
                it.copy(passwordError = passwordValidity.message)
            }
        }
        if (isEmailAndPasswordValid(emailValidity, passwordValidity).not()){
            return
        }

        viewModelScope.launch {
            _loginUiState.update { it.copy(isShowLoading = true)}
            val result = loginWithEmailAndPasswordUseCase(latestState.email, latestState.password)
            when(result){
                is Result.Fail -> _loginUiState.update { it.copy(generalError = result.message)}
                is Result.Success -> _loginUiState.update { it.copy(shouldGoToDashboard = true)}
            }
            _loginUiState.update { it.copy(isShowLoading = false)}

        }

    }

    private fun isEmailAndPasswordValid(emailValidity: Result<String>, passwordValidity: Result<String>): Boolean =
        emailValidity is Result.Success && passwordValidity is Result.Success

    private fun loginWithGoogle(){
        viewModelScope.launch {
            _loginUiState.update { it.copy(isShowLoading = true) }
            val signInResultResponse = loginWithGoogleUseCase.beginSignInRequest()
            _loginUiState.update { it.copy(signInResultResponse = signInResultResponse, isShowLoading = false) }
        }
    }

    private fun signInWithCredential(intent: Intent?){
        _loginUiState.update { it.copy(isShowLoading = true) }
        viewModelScope.launch {
            val result = loginWithGoogleUseCase.signInWithCredential(intent)
            when(result){
                is Result.Fail -> _loginUiState.update { it.copy(generalError = result.message) }
                is Result.Success -> _loginUiState.update { it.copy(shouldGoToDashboard = true) }
            }
            _loginUiState.update { it.copy(isShowLoading = false) }
        }
    }

    private fun forgotPassword(){

    }

}