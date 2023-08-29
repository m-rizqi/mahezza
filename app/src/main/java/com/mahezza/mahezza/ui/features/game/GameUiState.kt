package com.mahezza.mahezza.ui.features.game

import com.mahezza.mahezza.data.model.Child

data class GameUiState(
    val selectedChildren: List<Child> = emptyList()
)