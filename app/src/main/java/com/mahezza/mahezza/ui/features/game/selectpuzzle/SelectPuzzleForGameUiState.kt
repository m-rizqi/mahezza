package com.mahezza.mahezza.ui.features.game.selectpuzzle

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.ui.components.LayoutState

data class SelectPuzzleForGameUiState(
    val isShowLoading : Boolean = false,
    val generalMessage : StringResource? = null,

    val puzzleCardStateList: List<PuzzleCardState> = emptyList(),
    val puzzles : List<Puzzle> = emptyList(),
    val selectedPuzzle: Puzzle? = null,

    val puzzleLayoutState : LayoutState = LayoutState.Shimmer
){
    data class PuzzleCardState(
        val id : String,
        val name : String,
        val banner : String,
        val description : String,
        val isChecked : Boolean,
        val onClickListener : (PuzzleCardState) -> Unit
    )
}
