package com.mahezza.mahezza.ui.features.puzzle.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.domain.puzzle.GetRedeemedPuzzleUseCase
import com.mahezza.mahezza.ui.components.LayoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PuzzleListViewModel @Inject constructor(
    getRedeemedPuzzleUseCase: GetRedeemedPuzzleUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PuzzleListState())
    val uiState: StateFlow<PuzzleListState>
        get() = _uiState.asStateFlow()

    val redeemedPuzzleStates: Flow<List<Puzzle>> = getRedeemedPuzzleUseCase()
        .mapLatest { resultOfPuzzle ->
            when (resultOfPuzzle) {
                is com.mahezza.mahezza.domain.Result.Fail -> {
                    _uiState.update {
                        it.copy(
                            generalMessage = resultOfPuzzle.message,
                            puzzleLayoutState = LayoutState.Empty,
                        )
                    }
                    return@mapLatest emptyList();
                }

                is com.mahezza.mahezza.domain.Result.Success -> {
                    _uiState.update {
                        it.copy(
                            puzzleLayoutState = LayoutState.Content,
                        )
                    }
                    return@mapLatest resultOfPuzzle.data ?: emptyList();
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun onEvent(event: PuzzleListEvent){
        when(event){
            PuzzleListEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
        }
    }
}