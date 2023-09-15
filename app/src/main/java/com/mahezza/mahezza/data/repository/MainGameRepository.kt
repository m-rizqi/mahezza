package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.model.LastGameActivity
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.mapStatusToGameStatus
import com.mahezza.mahezza.data.model.toGame
import com.mahezza.mahezza.data.source.firebase.firestore.GameFirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainGameRepository @Inject constructor(
    private val gameFirebaseFirestore: GameFirebaseFirestore,
    private val childrenRepository: ChildrenRepository,
    private val puzzleRepository: PuzzleRepository
): GameRepository {
    override suspend fun saveGame(game: Game): Result<String> {
        val firebaseResult = gameFirebaseFirestore.saveGame(game.toGameRequest())
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!)
        return Result.Fail(firebaseResult.message)
    }

    override fun getLastGameActivities(parentId: String): Flow<Result<List<LastGameActivity>>> {
        return gameFirebaseFirestore.getLastGameActivities(parentId).map { firebaseResult ->
            if (!isFirebaseResultSuccess(firebaseResult)) return@map Result.Fail(firebaseResult.message)
            val lastGameActivityResponses = firebaseResult.data!!
            val lastGameActivities = mutableListOf<LastGameActivity>()
            lastGameActivityResponses.forEach { lastGameActivityResponse ->
                val children = mutableListOf<Child>()
                var puzzle : Puzzle? = null
                lastGameActivityResponse.childrenIds.forEach { childId ->
                    val result = childrenRepository.getChildById(parentId, childId)
                    if (result is Result.Success && result.data != null){
                        children.add(result.data)
                    }
                }
                puzzle = puzzleRepository.getPuzzleById(lastGameActivityResponse.puzzleId).data ?: return@forEach

                lastGameActivities.add(
                    LastGameActivity(
                        id = lastGameActivityResponse.id,
                        parentId = lastGameActivityResponse.parentId,
                        children = children,
                        puzzle = puzzle,
                        status = mapStatusToGameStatus(lastGameActivityResponse.status),
                        lastActivity = lastGameActivityResponse.lastActivity,
                        elapsedTime = lastGameActivityResponse.elapsedTime
                    )
                )
            }
            return@map Result.Success(
                data = lastGameActivities
            )
        }
    }

    override suspend fun getGame(parentId: String, gameId: String): Result<Game> {
        val firebaseResult = gameFirebaseFirestore.getGame(parentId, gameId)
        if (!isFirebaseResultSuccess(firebaseResult)) return Result.Fail(firebaseResult.message)
        val gameResponse = firebaseResult.data!!
        val children = getChildren(parentId, gameResponse.childrenIds)
        val puzzleResult = puzzleRepository.getPuzzleById(gameResponse.puzzleId)
        if (puzzleResult is Result.Fail || puzzleResult.data == null) return Result.Fail(puzzleResult.message)
        val puzzle = puzzleResult.data
        val game = gameResponse.toGame(children, puzzle)
        return Result.Success(game)
    }

    private suspend fun getChildren(parentId: String, childrenIds: List<String>): List<Child> {
        val children = mutableListOf<Child>()
        childrenIds.forEach { childId ->
            val child = childrenRepository.getChildById(parentId, childId)
            if (child is Result.Success && child.data != null) children.add(child.data)
        }
        return children
    }
}