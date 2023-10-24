package com.mahezza.mahezza.ui.features.home

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.ui.components.LayoutState

data class HomeUiState(

    val generalMessage : StringResource? = null,
    val lastGameActivityLayoutState : LayoutState = LayoutState.Shimmer,
    val puzzleLayoutState : LayoutState = LayoutState.Shimmer,
) {
    data class LastGameActivityState(
        val gameId : String,
        val puzzle: Puzzle,
        val lastActivity : String,
        val children : List<Child>,
        val elapsedTime: String,
        val onClick : () -> String,
    )
}