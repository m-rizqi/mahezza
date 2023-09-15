package com.mahezza.mahezza.ui.features.game

import android.graphics.Bitmap
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.ui.features.game.course.CourseUiState

sealed class GameEvent {
    class SetSelectedChildren(val children : List<Child>) : GameEvent()
    object OnGeneralMessageShowed : GameEvent()
    object OnSaveGameStatusAcknowledged : GameEvent()
    data class SaveGame(
        val puzzle: Puzzle? = null,
        val children : List<Child>? = null,
        val elapsedTime: String? = null,
        val twibbon : Bitmap? = null,
        val lastActivity : String? = null,
        val course : CourseUiState.CourseState? = null,
        val isGameFinished : Boolean = false,
        val status : Game.Status,
        val acknowledgeCode : GameUiState.AcknowledgeCode
    ) : GameEvent()
    object OnClearBitmapResource : GameEvent()
    class ResumeGame(val gameId : String) : GameEvent()
}
