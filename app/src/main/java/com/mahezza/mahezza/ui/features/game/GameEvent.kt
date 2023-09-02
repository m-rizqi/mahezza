package com.mahezza.mahezza.ui.features.game

import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle

sealed class GameEvent {
    class SetSelectedChildren(val children : List<Child>) : GameEvent()
    class SetSelectedPuzzle(val puzzle: Puzzle, val gameStepSaved: GameUiState.GameStepSaved) : GameEvent()
    class OnSavePlaySessionGame(val elapsedTime : String) : GameEvent()
    object OnGeneralMessageShowed : GameEvent()
    data class OnSaveGameStatusAcknowledged(val gameStepSaved: GameUiState.GameStepSaved) : GameEvent()
}
