package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.request.ContentRequest
import com.mahezza.mahezza.data.source.firebase.request.SubCourseRequest
import com.mahezza.mahezza.data.source.firebase.response.ContentResponse
import com.mahezza.mahezza.data.source.firebase.response.SubCourseResponse

data class SubCourse(
    val name : String,
    val id : String,
    val contents : List<Content>,
    val numberOfChallenges : Int,
    val illustrationUrl : String?,

    val progress : Float = 0.0f,
    val isCompleted : Boolean = false,
    val numberOfCompletedChallenges : Int = 0,
)

fun SubCourseResponse.toSubCourse() : SubCourse = SubCourse(
    name = this.name,
    numberOfChallenges = this.numberOfChallenges,
    id = this.subCourseId,
    contents = this.contents.map { it.toContent() },
    illustrationUrl = this.illustrationUrl
)

fun SubCourse.toSubCourseRequest() : SubCourseRequest = SubCourseRequest(
    contents = this.contents.map { it.toContentRequest() },
    name = this.name,
    numberOfChallenges = this.numberOfChallenges,
    subCourseId = this.id,
    illustrationUrl = this.illustrationUrl,

    progress = this.progress,
    completed = this.isCompleted,
    numberOfCompletedChallenges = this.numberOfCompletedChallenges,
)