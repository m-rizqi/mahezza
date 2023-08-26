package com.mahezza.mahezza.ui.features.redeempuzzle.redeem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.puzzle.RedeemPuzzleUseCase
import com.mahezza.mahezza.ui.features.redeempuzzle.PuzzleRedeemedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RedeemPuzzleViewModel @Inject constructor(
    private val redeemPuzzleUseCase: RedeemPuzzleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RedeemPuzzleUiState())
    val uiState : StateFlow<RedeemPuzzleUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event : RedeemPuzzleEvent){
        when(event){
            is RedeemPuzzleEvent.OnRedeemPuzzle -> redeemPuzzle(uiState.value.code)
            RedeemPuzzleEvent.OnRedeemSuccessDialogShowed -> _uiState.update { it.copy(puzzleRedeemedState = null, code = "") }
            RedeemPuzzleEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            is RedeemPuzzleEvent.OnCodeValueChanged -> _uiState.update { it.copy(code = event.value) }
        }
    }

    private fun redeemPuzzle(qrcode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isShowLoading = true) }
            val puzzleResult = redeemPuzzleUseCase(qrcode)
            when(puzzleResult){
                is Result.Fail -> _uiState.update { it.copy(generalMessage = puzzleResult.message) }
                is Result.Success -> {
                    puzzleResult.data?.let { puzzle ->
                        _uiState
                            .update {
                                it.copy(
                                    puzzleRedeemedState = PuzzleRedeemedState(
                                        name = puzzle.name,
                                        banner = puzzle.banner
                                    )
                                )
                            }
                    }
                }
            }
            _uiState.update { it.copy(isShowLoading = false) }
        }
    }
}