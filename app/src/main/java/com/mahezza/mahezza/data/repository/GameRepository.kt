package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.model.LastGameActivity
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun saveGame(game: Game): Result<String>
    fun getLastGameActivities(parentId : String) : Flow<Result<List<LastGameActivity>>>
    suspend fun getGame(parentId: String, gameId : String) : Result<Game>
}