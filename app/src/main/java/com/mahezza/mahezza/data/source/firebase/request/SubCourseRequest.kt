package com.mahezza.mahezza.data.source.firebase.request

data class SubCourseRequest(
    val content : List<ContentRequest>,
    val name : String,
    val numberOfChallenges : Int,
    val subCourseId : String,
    val illustrationUrl : String?,

    val progress : Float,
    val isCompleted : Boolean,
    val numberOfCompletedChallenges : Int,
)