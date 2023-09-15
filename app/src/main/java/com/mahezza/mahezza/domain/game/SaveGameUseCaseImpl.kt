package com.mahezza.mahezza.domain.game

import android.graphics.Bitmap
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Content
import com.mahezza.mahezza.data.model.Course
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.data.model.SubCourse
import com.mahezza.mahezza.data.repository.GameRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.data.source.firebase.storage.FirebaseStorage
import com.mahezza.mahezza.data.source.firebase.storage.ImageRequest
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.ui.features.game.course.CourseUiState
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
        val status = saveGameState.status ?: Game.Status.SelectChild

        val twibbonFileName = "${saveGameState.puzzle.name}-${saveGameState.children.joinToString { it.name }}"
        val twibbonUrl = if (saveGameState.twibbon == null) saveGameState.twibbonUrl else saveTwibbonAndGetUrl(saveGameState.twibbon, twibbonFileName)

        var course : Course? = null
        saveGameState.courseState?.let { courseState ->
            course = Course(
                name = courseState.name,
                banner = courseState.banner,
                id = courseState.id,
                puzzleId = courseState.puzzleId,
                subCourses = courseState.subCourseStates.map { subCourseState ->
                    SubCourse(
                        name = subCourseState.name,
                        numberOfChallenges = subCourseState.numberOfChallenges,
                        id = subCourseState.id,
                        illustrationUrl = subCourseState.illustrationUrl,

                        progress = subCourseState.progress,
                        isCompleted = subCourseState.isCompleted,
                        numberOfCompletedChallenges = subCourseState.numberOfCompletedChallenges,
                        contents = subCourseState.contentStates.map { contentState ->
                            when(contentState){
                                is CourseUiState.ContentState.ChallengeState -> Content.Challenge(
                                    position = contentState.position,
                                    id = contentState.id,
                                    instruction = contentState.instruction,
                                    title = contentState.title,
                                    isCompleted = contentState.isCompleted,
                                    challengeNumber = contentState.challengeNumber,
                                    numberOfChallenges = contentState.numberOfChallenges,
                                )
                                is CourseUiState.ContentState.ImageState -> Content.Image(
                                    position = contentState.position,
                                    url = contentState.url
                                )
                                is CourseUiState.ContentState.ScriptState -> Content.Script(
                                    position = contentState.position,
                                    text = contentState.text
                                )
                                is CourseUiState.ContentState.VideoState -> Content.Video(
                                    position = contentState.position,
                                    url = contentState.url
                                )
                            }
                        }
                    )
                }
            )
        }

        val game = Game(
            id = id,
            parentId = parentId,
            children = saveGameState.children,
            puzzle = saveGameState.puzzle,
            status = status,
            lastActivity = saveGameState.lastActivity,
            elapsedTime = saveGameState.elapsedTime,
            twibbonUrl = twibbonUrl,
            course = course
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