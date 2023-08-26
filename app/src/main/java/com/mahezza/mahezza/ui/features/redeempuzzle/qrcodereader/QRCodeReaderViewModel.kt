package com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.puzzle.RedeemPuzzleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QRCodeReaderViewModel @Inject constructor(
    private val redeemPuzzleUseCase: RedeemPuzzleUseCase
) : ViewModel(){

    private val _uiState = MutableStateFlow(QRCodeReaderUiState())
    val uiState : StateFlow<QRCodeReaderUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event : QRCodeReaderEvent){
        when(event){
            is QRCodeReaderEvent.OnRedeemPuzzle -> redeemPuzzle(uiState.value.qrcode)
            QRCodeReaderEvent.OnRedeemSuccessDialogShowed -> _uiState.update { it.copy(puzzleRedeemedState = null, qrcode = "") }
            QRCodeReaderEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            is QRCodeReaderEvent.OnQRScanned -> _uiState.update { it.copy(qrcode = event.value) }
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
                                    puzzleRedeemedState = QRCodeReaderUiState.PuzzleRedeemedState(
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