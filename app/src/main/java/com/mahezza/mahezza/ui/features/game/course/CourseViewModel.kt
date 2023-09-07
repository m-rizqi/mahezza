package com.mahezza.mahezza.ui.features.game.course

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Content
import com.mahezza.mahezza.data.model.Course
import com.mahezza.mahezza.data.repository.CourseRepository
import com.mahezza.mahezza.ui.components.LayoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseViewModel @Inject constructor(
    private val courseRepository: CourseRepository
): ViewModel() {

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState : StateFlow<CourseUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event: CourseEvent){
        when(event){
            CourseEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            is CourseEvent.SetPuzzleId -> setPuzzleId(event.puzzleId)
            CourseEvent.OnSubCourseDetailOpened -> _uiState.update { it.copy(openSubCourseDetail = null) }
            is CourseEvent.OpenSubCourse -> _uiState.update { it.copy(openedSubCourseState = findSubCourse(event.subCourseId)) }
            is CourseEvent.OnSubCourseCompleted -> onSubCourseCompleted(event.subCourseId)
        }
    }

    private fun findSubCourse(subCourseId: String): CourseUiState.SubCourseState? {
        return uiState.value.courseState?.subCourseStates?.find { it.id == subCourseId }
    }

    private fun setPuzzleId(puzzleId : String){
        viewModelScope.launch {
            courseRepository.getCourseByPuzzleId(puzzleId).collect{result ->
                _uiState.update { it.copy(layoutState = LayoutState.Shimmer) }
                when(result){
                    is Result.Fail -> _uiState.update {
                        it.copy(
                            generalMessage = result.message,
                            layoutState = LayoutState.Empty
                        )
                    }
                    is Result.Success -> {
                        result.data?.let { course ->
                            val courseState = mapCourseToCourseState(course)
                            _uiState.update { it.copy(
                                courseState = courseState,
                                layoutState = if (courseState.subCourseStates.isEmpty()) LayoutState.Empty else LayoutState.Content
                            ) }
                        }
                    }
                }
            }
        }
    }

    private fun openSubCourseDetail(subCourseId : String){
        _uiState.update { it.copy(openSubCourseDetail = CourseUiState.OpenSubCourseDetail(subCourseId)) }
    }

    private fun mapCourseToCourseState(course: Course): CourseUiState.CourseState {
        val latestCourseState = uiState.value.courseState
        return CourseUiState.CourseState(
            id = course.id,
            name = course.name,
            banner = course.banner,
            puzzleId = course.puzzleId,
            subCourseStates = course.subCourses.map {subCourse ->
                val latestSubCourseState = latestCourseState?.subCourseStates?.find { it.id == subCourse.id }
                var challengeNumber = 0
                CourseUiState.SubCourseState(
                    id = subCourse.id,
                    name = subCourse.name,
                    progress = latestSubCourseState?.progress ?: 0f,
                    isCompleted = latestSubCourseState?.isCompleted ?: false,
                    numberOfCompletedChallenges = latestSubCourseState?.numberOfCompletedChallenges ?: 0,
                    numberOfChallenges = subCourse.numberOfChallenges,
                    illustrationUrl = subCourse.illustrationUrl,
                    onClick = {
                              openSubCourseDetail(subCourse.id)
                    },
                    contentStates = subCourse.contents.sortedBy { it.position }.map { content ->
                        when(content){
                            is Content.Challenge -> {
                                val latestChallenge = latestSubCourseState?.contentStates?.find { contentState ->
                                    contentState is CourseUiState.ContentState.ChallengeState && contentState.id == content.id
                                } as CourseUiState.ContentState.ChallengeState?
                                challengeNumber++
                                CourseUiState.ContentState.ChallengeState(
                                    position = content.position,
                                    id = content.id,
                                    instruction = content.instruction,
                                    title = content.title,
                                    isCompleted = latestChallenge?.isCompleted ?: false,
                                    challengeNumber = challengeNumber,
                                    numberOfChallenges = subCourse.numberOfChallenges,
                                    onCompletionClick = {
                                        onChallengeCompletionChange(content.id)
                                    }
                                )
                            }
                            is Content.Image -> CourseUiState.ContentState.ImageState(
                                position = content.position,
                                url = content.url
                            )
                            is Content.Script -> CourseUiState.ContentState.ScriptState(
                                position = content.position,
                                text = content.text
                            )
                            is Content.Video -> CourseUiState.ContentState.VideoState(
                                position = content.position,
                                url = content.url
                            )
                        }
                    }
                )
            }
        )
    }

    private fun onChallengeCompletionChange(challengeId: String): Unit {
        val challengeState =
            findChallengeStateById(uiState.value.courseState, challengeId) ?: return
        val currentUiState = uiState.value
        val currentCourseState = currentUiState.courseState ?: return

        val updatedCourseState = currentCourseState.copy(
            subCourseStates = currentCourseState.subCourseStates.map { subCourseState ->
                val updatedContentStates = subCourseState.contentStates.map { contentState ->
                    if (contentState is CourseUiState.ContentState.ChallengeState &&
                        contentState.id == challengeId
                    ) {
                        val updatedIsCompleted = !contentState.isCompleted
                        contentState.copy(isCompleted = updatedIsCompleted) // Update ChallengeState
                    } else {
                        contentState
                    }
                }

                val updatedNumberOfCompletedChallenges = updatedContentStates
                    .filterIsInstance<CourseUiState.ContentState.ChallengeState>()
                    .count { it.isCompleted }

                val progress = updatedNumberOfCompletedChallenges.toFloat() / subCourseState.numberOfChallenges.toFloat()

                subCourseState.copy(
                    contentStates = updatedContentStates,
                    numberOfCompletedChallenges = updatedNumberOfCompletedChallenges,
                    progress = progress
                )
            }
        )

        _uiState.update {
            it.copy(courseState = updatedCourseState)
        }

    }

    private fun onSubCourseCompleted(subCourseId: String) {
        val currentUiState = uiState.value
        val currentCourseState = currentUiState.courseState ?: return

        val updatedCourseState = currentCourseState.copy(
            subCourseStates = currentCourseState.subCourseStates.map { subCourseState ->

                val isCompleted = if (subCourseId == subCourseState.id) true else subCourseState.isCompleted

                subCourseState.copy(
                    isCompleted = isCompleted
                )
            }
        )

        val numberOfSubCourseCompleted = updatedCourseState.subCourseStates.count { it.isCompleted }
        val isCourseCompleted = numberOfSubCourseCompleted == updatedCourseState.subCourseStates.size
        _uiState.update {
            it.copy(
                courseState = updatedCourseState,
                isCourseCompleted = isCourseCompleted
            )
        }
    }

    private fun findChallengeStateById(courseState: CourseUiState.CourseState?, targetId: String): CourseUiState.ContentState.ChallengeState? {
        if (courseState == null) return null
        for (subCourseState in courseState.subCourseStates) {
            for (contentState in subCourseState.contentStates) {
                if (contentState is CourseUiState.ContentState.ChallengeState && contentState.id == targetId) {
                    return contentState
                }
            }
        }
        return null
    }
}