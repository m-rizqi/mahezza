package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.request.GameRequest

interface GameFirebaseFirestore {
    companion object {
        const val GAME_PATH = "games"
    }

    suspend fun saveGame(gameRequest: GameRequest) : FirebaseResult<String>
}