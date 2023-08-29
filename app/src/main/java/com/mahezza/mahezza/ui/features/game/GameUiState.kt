package com.mahezza.mahezza.ui.features.game

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle

data class GameUiState(
    val children: List<Child> = emptyList(),
    val puzzle: Puzzle? = null,

    val isLoading : Boolean = false,
    val generalMessage : StringResource? = null,
    val isSaveGameSuccess : Boolean = false
)