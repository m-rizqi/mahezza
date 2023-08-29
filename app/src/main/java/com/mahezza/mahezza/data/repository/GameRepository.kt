package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Game

interface GameRepository {
    suspend fun saveGame(game: Game): Result<String>
}