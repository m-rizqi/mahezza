package com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader

import com.mahezza.mahezza.common.StringResource

data class QRCodeReaderUiState(

    val qrcode : String = "",

    val generalMessage: StringResource? = null,
    val isShowLoading: Boolean = false,

    val puzzleRedeemedState : PuzzleRedeemedState? = null
){
    data class PuzzleRedeemedState(
        val name : String,
        val banner : String
    )
}