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
        val illustrationUrl : String,
        val contentStates : List<ContentState>,
        val onClick : () -> Unit
    )
    sealed class ContentState(
        open val position : Int
    ) {
        data class ChallengeState(
            override val position: Int,
            val id : String,
            val instruction : String,
            val title : String,
            var isCompleted: Boolean,
            val challengeNumber : Int,
            val numberOfChallenges: Int,
            val onCompletionClick: () -> Unit
        ) : ContentState(position = position)
        data class VideoState(
            override val position: Int,
            val url : String,
        ) : ContentState(position = position)
        data class ImageState(
            override val position: Int,
            val url : String,
        ) : ContentState(position = position)
        data class ScriptState(
            override val position: Int,
            val text : String,
        ) : ContentState(position = position)
    }

}