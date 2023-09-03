package com.mahezza.mahezza.domain.game

import android.graphics.Bitmap
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.common.loadBitmapFromUri
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.repository.GameRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.data.source.firebase.storage.FirebaseStorage
import com.mahezza.mahezza.data.source.firebase.storage.ImageRequest
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.user.CreateProfileUseCase
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject

class SaveGameUseCaseImpl @Inject constructor(
    private val dataStore: MahezzaDataStore,
    private val gameRepository: GameRepository,
    private val firebaseStorage: FirebaseStorage
) : SaveGameUseCase {

    override suspend fun invoke(saveGameState: SaveGameUseCase.SaveGameState): Result<Game> {
        val id = saveGameState.id ?: UUID.randomUUID().toString()
        val parentId = saveGameState.parentId ?:
            dataStore.firebaseUserIdPreference.first() ?:
            return Result.Fail(StringResource.StringResWithParams(R.string.user_id_is_not_found))
        val status = saveGameState.status ?: Game.Status.OnGoing

        val twibbonFileName = "${saveGameState.puzzle.name}-${saveGameState.children.joinToString { it.name }}"
        val twibbonUrl = if (saveGameState.twibbon == null) saveGameState.twibbonUrl else saveTwibbonAndGetUrl(saveGameState.twibbon, twibbonFileName)

        val game = Game(
            id = id,
            parentId = parentId,
            children = saveGameState.children,
            puzzle = saveGameState.puzzle,
            status = status,
            lastActivity = saveGameState.lastActivity,
            elapsedTime = saveGameState.elapsedTime,
            twibbonUrl = twibbonUrl
        )

        val repositoryResult = gameRepository.saveGame(game)
        return when(repositoryResult){
            is com.mahezza.mahezza.data.Result.Fail -> Result.Fail(repositoryResult.message)
            is com.mahezza.mahezza.data.Result.Success -> Result.Success(game)
        }
    }

    private suspend fun saveTwibbonAndGetUrl(bitmap: Bitmap, fileName : String): String? {
        val imageRequest = ImageRequest(
            id = fileName,
            bitmap = bitmap
        )
        val path = "${FirebaseStorage.USER_PATH}/${FirebaseStorage.GAME_PATH}"
        val firebaseResult = firebaseStorage.insertOrUpdateImage(path, imageRequest)
        return firebaseResult.data
    }
}