package com.mahezza.mahezza.ui.features.puzzle.list

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.ui.components.LayoutState

data class PuzzleListState (
    val generalMessage : StringResource? = null,
    val puzzleLayoutState : LayoutState = LayoutState.Shimmer,
)