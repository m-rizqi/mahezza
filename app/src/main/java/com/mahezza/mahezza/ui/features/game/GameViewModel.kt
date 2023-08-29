package com.mahezza.mahezza.ui.features.game

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.game.SaveGameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val saveGameUseCase: SaveGameUseCase,
    private val resource: Resources
): ViewModel(){
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState : StateFlow<GameUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event: GameEvent){
        when(event){
            is GameEvent.SetSelectedChildren -> _uiState.update { it.copy(children = event.children) }
            is GameEvent.SetSelectedPuzzle -> {
                _uiState.update { it.copy(puzzle = event.puzzle) }
                saveGame(
                    lastActivity = resource.getString(R.string.playing_puzzle_name, uiState.value.puzzle!!.name)
                )
            }

            GameEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            GameEvent.OnSaveGameStatusAcknowledged -> _uiState.update { it.copy(isSaveGameSuccess = false) }
        }
    }

    private fun saveGame(lastActivity : String) {
        if (uiState.value.puzzle == null){
            _uiState.update { it.copy(generalMessage = StringResource.StringResWithParams(R.string.select_puzzle_first)) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, isSaveGameSuccess = false) }
            val result=  saveGameUseCase(
                SaveGameUseCase.SaveGameState(
                    children = uiState.value.children,
                    puzzle = uiState.value.puzzle!!,
                    lastActivity = lastActivity
                )
            )
            when(result){
                is Result.Fail -> _uiState.update { it.copy(generalMessage = result.message) }
                is Result.Success -> {
                    _uiState.update { it.copy(isSaveGameSuccess = true) }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}