package com.mahezza.mahezza.ui.features.redeempuzzle.redeem

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.ui.features.redeempuzzle.PuzzleRedeemedState

data class RedeemPuzzleUiState(
    val code : String = "",

    val generalMessage: StringResource? = null,
    val isShowLoading: Boolean = false,

    val puzzleRedeemedState : PuzzleRedeemedState? = null
)