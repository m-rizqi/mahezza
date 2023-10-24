package com.mahezza.mahezza.data.source.firebase.response

data class ContentResponse(
    val id : String? = null,
    val content : String = "",
    val title : String? = null,
    val type : String = "",
    val isCompleted : Boolean?,
    val challengeNumber : Int?,
    val numberOfChallenges : Int?,
)