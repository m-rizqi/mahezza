package com.mahezza.mahezza.ui.features.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.auth.ValidateEmailUseCase
import com.mahezza.mahezza.domain.auth.ValidatePasswordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
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
            else -> Unit
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

    }

    private fun isEmailAndPasswordValid(emailValidity: Result<String>, passwordValidity: Result<String>): Boolean =
        emailValidity is Result.Success && passwordValidity is Result.Success

    private fun loginWithGoogle(){

    }

    private fun forgotPassword(){

    }

}