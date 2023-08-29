package com.mahezza.mahezza.ui.features.game.selectpuzzle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.puzzle.GetRedeemedPuzzleUseCase
import com.mahezza.mahezza.ui.components.LayoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPuzzleForGameViewModel @Inject constructor(
    private val getRedeemedPuzzleUseCase: GetRedeemedPuzzleUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SelectPuzzleForGameUiState())
    val uiState : StateFlow<SelectPuzzleForGameUiState>
        get() = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getRedeemedPuzzleUseCase().collectLatest {resultOfPuzzle ->
                _uiState.update { it.copy(puzzleLayoutState = LayoutState.Shimmer) }
                when(resultOfPuzzle){
                    is Result.Fail -> {
                        _uiState.update { it.copy(
                            puzzleLayoutState = LayoutState.Empty,
                            puzzles = emptyList(),
                            puzzleCardStateList = emptyList(),
                            generalMessage = resultOfPuzzle.message
                        ) }
                    }
                    is Result.Success -> {
                        val puzzles = resultOfPuzzle.data ?: emptyList()
                        var newPuzzleCardStates = puzzles.map { puzzle ->
                            val previousPuzzleCardState = _uiState.value.puzzleCardStateList.find { it.id == puzzle.id }
                            SelectPuzzleForGameUiState.PuzzleCardState(
                                id = puzzle.id,
                                name = puzzle.name,
                                banner = puzzle.banner,
                                description = puzzle.description,
                                isChecked = previousPuzzleCardState?.isChecked ?: false,
                                onClickListener = { puzzleCardState ->
                                    onPuzzleClickListener(puzzleCardState)
                                }
                            )
                        }
                        newPuzzleCardStates = checkOrSetPuzzleForSelectedRequirement(newPuzzleCardStates)
                        _uiState.update { it.copy(
                            puzzleLayoutState = if (newPuzzleCardStates.isEmpty()) LayoutState.Empty else LayoutState.Content,
                            puzzles = puzzles ,
                            puzzleCardStateList = newPuzzleCardStates,
                        ) }
                    }
                }
            }
        }
    }

    private fun checkOrSetPuzzleForSelectedRequirement(newPuzzleCardStates: List<SelectPuzzleForGameUiState.PuzzleCardState>): List<SelectPuzzleForGameUiState.PuzzleCardState> {
        val selectedCount = newPuzzleCardStates.count { it.isChecked }
        return when {
            selectedCount < 1 -> setFirstPuzzleToChecked(newPuzzleCardStates)
            selectedCount == 1 -> newPuzzleCardStates
            else -> setJustOnePuzzleChecked(newPuzzleCardStates)
        }
    }

    private fun setJustOnePuzzleChecked(newPuzzleCardStates: List<SelectPuzzleForGameUiState.PuzzleCardState>): List<SelectPuzzleForGameUiState.PuzzleCardState> {
        var isCheckedPuzzleFound = false
        return newPuzzleCardStates.map { puzzleCardState ->
            if (!puzzleCardState.isChecked) return@map puzzleCardState
            if (isCheckedPuzzleFound) return@map puzzleCardState.copy(isChecked = false)
            isCheckedPuzzleFound = true
            return@map puzzleCardState
        }
    }

    private fun setFirstPuzzleToChecked(newPuzzleCardStates: List<SelectPuzzleForGameUiState.PuzzleCardState>): List<SelectPuzzleForGameUiState.PuzzleCardState> {
        val copyOfNewPuzzleCardStates = newPuzzleCardStates.toMutableList()
        copyOfNewPuzzleCardStates[0] = copyOfNewPuzzleCardStates[0].copy(isChecked = true)
        return copyOfNewPuzzleCardStates.toList()
    }

    private fun onPuzzleClickListener(clickedPuzzleCard: SelectPuzzleForGameUiState.PuzzleCardState) {
        val updatedList = uiState.value.puzzleCardStateList.map { puzzleCard ->
            if (puzzleCard.id == clickedPuzzleCard.id) {
                puzzleCard.copy(isChecked = true)
            } else {
                puzzleCard.copy(isChecked = false)
            }
        }

        _uiState.update {
            it.copy(puzzleCardStateList = updatedList)
        }
    }

    fun onEvent(event: SelectPuzzleForGameEvent){
        when(event){
            SelectPuzzleForGameEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            SelectPuzzleForGameEvent.OnNextListener -> onNext()
        }
    }

    private fun onNext() {
        findSelectedPuzzleOrShowMessage()?.let { selectedPuzzle ->
            _uiState.update {
                it.copy(
                    selectedPuzzle = selectedPuzzle
                )
            }
        }
    }

    private fun findSelectedPuzzleOrShowMessage() : Puzzle?{
        val selectedPuzzleCardState = uiState.value.puzzleCardStateList.find { it.isChecked }
        val selectedPuzzle = uiState.value.puzzles.find { it.id == selectedPuzzleCardState?.id }
        if (selectedPuzzle == null) _uiState.update { it.copy(generalMessage = StringResource.StringResWithParams(R.string.select_puzzle_first)) }
        return selectedPuzzle
    }
}