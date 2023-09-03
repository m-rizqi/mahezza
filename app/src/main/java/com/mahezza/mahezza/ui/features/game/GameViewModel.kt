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
            GameEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            GameEvent.OnSaveGameStatusAcknowledged -> _uiState.update { it.copy(acknowledgeCode = null) }
            is GameEvent.SaveGame -> saveGame(event)
        }
    }

    private fun saveGame(event: GameEvent.SaveGame){
        updateGameUiStateForSaving(event)
        if (uiState.value.puzzle == null){
            _uiState.update { it.copy(generalMessage = StringResource.StringResWithParams(R.string.select_puzzle_first)) }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, acknowledgeCode = null) }
            val result=  saveGameUseCase(
                SaveGameUseCase.SaveGameState(
                    id = uiState.value.id,
                    children = uiState.value.children,
                    puzzle = uiState.value.puzzle!!,
                    lastActivity = uiState.value.lastActivity,
                    elapsedTime = uiState.value.elapsedTime,
                    twibbon = uiState.value.twibbon
                )
            )
            when(result){
                is Result.Fail -> _uiState.update { it.copy(generalMessage = result.message) }
                is Result.Success -> {
                    val savedGame = result.data
                    _uiState.update { it.copy(
                        id = savedGame?.id,
                        twibbonUrl = savedGame?.twibbonUrl,
                        acknowledgeCode = event.acknowledgeCode
                    ) }
                }
            }
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    private fun updateGameUiStateForSaving(event: GameEvent.SaveGame){
        with(event){
            children?.let { children -> _uiState.update { it.copy(children = children) } }
            puzzle?.let { puzzle -> _uiState.update { it.copy(puzzle = puzzle) } }
            elapsedTime?.let { elapsedTime -> _uiState.update { it.copy(elapsedTime = elapsedTime) } }
            twibbon?.let { twibbon -> _uiState.update { it.copy(twibbon = twibbon) } }
            lastActivity?.let { lastActivity -> _uiState.update { it.copy(lastActivity = lastActivity) } }
        }
    }

    fun getChildren() = uiState.value.children

    fun getPuzzle() = uiState.value.puzzle
}