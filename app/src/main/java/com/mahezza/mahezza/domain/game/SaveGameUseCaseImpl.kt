package com.mahezza.mahezza.domain.game

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.repository.GameRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.Result
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class SaveGameUseCaseImpl @Inject constructor(
    private val dataStore: MahezzaDataStore,
    private val gameRepository: GameRepository
) : SaveGameUseCase {
    override suspend fun invoke(saveGameState: SaveGameUseCase.SaveGameState): Result<String> {
        val id = saveGameState.id ?: UUID.randomUUID().toString()
        val parentId = saveGameState.parentId ?:
            dataStore.firebaseUserIdPreference.first() ?:
            return Result.Fail(StringResource.StringResWithParams(R.string.user_id_is_not_found))
        val status = saveGameState.status ?: Game.Status.OnGoing

        val game = Game(
            id = id,
            parentId = parentId,
            children = saveGameState.children,
            puzzle = saveGameState.puzzle,
            status = status,
            lastActivity = saveGameState.lastActivity
        )

        val repositoryResult = gameRepository.saveGame(game)
        return when(repositoryResult){
            is com.mahezza.mahezza.data.Result.Fail -> Result.Fail(repositoryResult.message)
            is com.mahezza.mahezza.data.Result.Success -> Result.Success(repositoryResult.data ?: id)
        }
    }
}