package com.mahezza.mahezza.domain.game

import android.graphics.Bitmap
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.ui.features.game.course.CourseUiState

interface SaveGameUseCase {
    suspend operator fun invoke(saveGameState: SaveGameState) : Result<Game>

    data class SaveGameState(
        val id : String? = null,
        val parentId : String? = null,
        val children : List<Child>,
        val puzzle : Puzzle,
        val status : Game.Status? = null,
        val lastActivity : String,
        val elapsedTime : String = "00:00:00",
        val twibbon : Bitmap? = null,
        val twibbonUrl : String? = null,
        val courseState: CourseUiState.CourseState? = null,
    )
}