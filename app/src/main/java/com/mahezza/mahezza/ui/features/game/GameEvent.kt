package com.mahezza.mahezza.ui.features.game

import android.graphics.Bitmap
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle

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
        val acknowledgeCode : GameUiState.AcknowledgeCode
    ) : GameEvent()

}
