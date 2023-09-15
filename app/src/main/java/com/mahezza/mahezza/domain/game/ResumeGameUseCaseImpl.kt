package com.mahezza.mahezza.domain.game

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.repository.GameRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.Result
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ResumeGameUseCaseImpl @Inject constructor(
    private val dataStore: MahezzaDataStore,
    private val gameRepository: GameRepository,
) : ResumeGameUseCase {
    override suspend fun invoke(gameId: String): Result<Game> {
        val parentId = dataStore.firebaseUserIdPreference.first()
            ?: return Result.Fail(StringResource.StringResWithParams(R.string.user_id_is_not_found))
        val result = gameRepository.getGame(parentId, gameId)
        return when(result){
            is com.mahezza.mahezza.data.Result.Fail -> Result.Fail(result.message)
            is com.mahezza.mahezza.data.Result.Success -> {
                if (result.data == null) return Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))
                return Result.Success(result.data)
            }
        }
    }

}