package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.source.firebase.firestore.GameFirebaseFirestore
import javax.inject.Inject

class MainGameRepository @Inject constructor(
    private val gameFirebaseFirestore: GameFirebaseFirestore
): GameRepository {
    override suspend fun saveGame(game: Game): Result<String> {
        val firebaseResult = gameFirebaseFirestore.saveGame(game.toGameRequest())
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!)
        return Result.Fail(firebaseResult.message)
    }
}