package com.mahezza.mahezza.ui.features.game

import androidx.lifecycle.ViewModel
import com.mahezza.mahezza.domain.auth.LoginWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class GameViewModel : ViewModel(){
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState : StateFlow<GameUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event: GameEvent){
        when(event){
            is GameEvent.SetSelectedChildren -> _uiState.update { it.copy(selectedChildren = event.children) }
        }
    }
}