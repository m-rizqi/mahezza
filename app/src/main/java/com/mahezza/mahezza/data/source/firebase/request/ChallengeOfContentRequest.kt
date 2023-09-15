package com.mahezza.mahezza.data.source.firebase.request

data class ChallengeOfContentRequest(
    val position : Int,
    val challengeId : String,
    val instruction : String,
    val title : String,
    var completed: Boolean,
    val challengeNumber : Int,
    val numberOfChallenges: Int,
)
