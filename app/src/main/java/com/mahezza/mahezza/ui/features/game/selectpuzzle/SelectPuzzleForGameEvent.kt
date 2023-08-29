package com.mahezza.mahezza.ui.features.game.selectpuzzle

sealed class SelectPuzzleForGameEvent {
    object OnGeneralMessageShowed : SelectPuzzleForGameEvent()
    object OnNextListener : SelectPuzzleForGameEvent()
}