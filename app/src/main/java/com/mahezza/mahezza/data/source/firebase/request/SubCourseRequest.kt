package com.mahezza.mahezza.data.source.firebase.request

data class SubCourseRequest(
    val contents : List<ContentRequest>,
    val name : String,
    val numberOfChallenges : Int,
    val subCourseId : String,
    val illustrationUrl : String?,
    val progress : Float,
    val completed : Boolean,
    val numberOfCompletedChallenges : Int,
)