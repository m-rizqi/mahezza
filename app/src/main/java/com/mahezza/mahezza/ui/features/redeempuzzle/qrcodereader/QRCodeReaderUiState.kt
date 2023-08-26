package com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.ui.features.redeempuzzle.PuzzleRedeemedState

data class QRCodeReaderUiState(

    val qrcode : String = "",

    val generalMessage: StringResource? = null,
    val isShowLoading: Boolean = false,

    val puzzleRedeemedState : PuzzleRedeemedState? = null
)