package com.mahezza.mahezza.ui.features.game

import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle

sealed class GameEvent {
    class SetSelectedChildren(val children : List<Child>) : GameEvent()
    class SetSelectedPuzzle(val puzzle: Puzzle) : GameEvent()
    object OnGeneralMessageShowed : GameEvent()
    object OnSaveGameStatusAcknowledged : GameEvent()
}
