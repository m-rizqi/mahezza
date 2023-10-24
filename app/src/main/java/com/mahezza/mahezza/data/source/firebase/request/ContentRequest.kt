package com.mahezza.mahezza.data.source.firebase.request

data class ContentRequest(
    val id : String? = null,
    val content : String = "",
    val title : String? = null,
    val isCompleted : Boolean?,
    val challengeNumber : Int?,
    val numberOfChallenges : Int?,
    val type : String = "",
)