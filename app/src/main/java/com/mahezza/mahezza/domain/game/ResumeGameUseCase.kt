package com.mahezza.mahezza.domain.game

import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.domain.Result

interface ResumeGameUseCase {
    suspend operator fun invoke(gameId : String) : Result<Game>
}