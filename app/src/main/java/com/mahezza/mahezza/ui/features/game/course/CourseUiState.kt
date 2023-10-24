package com.mahezza.mahezza.ui.features.game.course

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.ui.components.LayoutState

data class CourseUiState(

    val courseState : CourseState? = null,

    val openedSubCourseState: SubCourseState? = null,

    val isCourseCompleted : Boolean = false,

    val isShowLoading : Boolean = false,
    val isShowShimmer : Boolean = false,
    val generalMessage : StringResource? = null,
    val layoutState : LayoutState = LayoutState.Shimmer,
    val openSubCourseDetail: OpenSubCourseDetail? = null
){
    data class OpenSubCourseDetail(val subCourseId : String)
    data class CourseState(
        val id : String,
        val puzzleId : String,
        val name: String,
        val banner : String,
        val subCourseStates : List<SubCourseState>
    ){
        fun getCompletedChallenge() : Int {
            return this.subCourseStates.sumBy { subCourseState ->
                subCourseState.contentStates.count { contentState ->
                    contentState is ContentState.ChallengeState && contentState.isCompleted
                }
            }
        }
    }
    data class SubCourseState(
        val id : String,
        val name : String,
        val progress : Float,
        val isCompleted : Boolean,
        val numberOfCompletedChallenges : Int,
        val numberOfChallenges : Int,
        val illustrationUrl : String?,
        val contentStates : List<ContentState>,
        val onClick : () -> Unit
    )
    sealed class ContentState(
        open val id : String?,
        open val content : String,
        open val title : String?,
        open val isCompleted : Boolean?,
        open val challengeNumber : Int?,
        open val numberOfChallenges : Int?,
    ) {
        data class ChallengeState(
            override val id: String,
            override val content: String,
            override val title: String,
            override val isCompleted: Boolean,
            override val challengeNumber: Int,
            override val numberOfChallenges: Int,
            val onCompletionClick: () -> Unit
        ) : ContentState(id, content, title, isCompleted, challengeNumber, numberOfChallenges)
        data class VideoState(
            override val id: String?,
            override val content: String,
            override val title: String?,
        ) : ContentState(id, content, title, null, null, null)
        data class ImageState(
            override val id: String?,
            override val content: String,
            override val title: String?,
        ) : ContentState(id, content, title, null, null, null)
        data class ScriptState(
            override val id: String?,
            override val content: String,
            override val title: String?,
        ) : ContentState(id, content, title, null, null, null)
    }

}