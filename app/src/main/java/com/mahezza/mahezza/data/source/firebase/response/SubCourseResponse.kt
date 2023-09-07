package com.mahezza.mahezza.data.source.firebase.response

data class SubCourseResponse(
    val content : List<ContentResponse> = emptyList(),
    val name : String = "",
    val numberOfChallenges : Int = 0,
    val subCourseId : String = "",
    val illustrationUrl : String? = null,
)