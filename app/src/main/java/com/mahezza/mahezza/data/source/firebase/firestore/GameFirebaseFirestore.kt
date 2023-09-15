package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.request.GameRequest
import com.mahezza.mahezza.data.source.firebase.response.GameResponse
import com.mahezza.mahezza.data.source.firebase.response.LastGameActivityResponse
import kotlinx.coroutines.flow.Flow

interface GameFirebaseFirestore {
    companion object {
        const val GAME_PATH = "games"
        const val STATUS_FIELD = "status"
    }

    suspend fun saveGame(gameRequest: GameRequest) : FirebaseResult<String>
    fun getLastGameActivities(parentId : String) : Flow<FirebaseResult<out List<LastGameActivityResponse>>>
    suspend fun getGame(parentId:String, gameId : String) : FirebaseResult<GameResponse>
}