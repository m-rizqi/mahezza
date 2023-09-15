package com.mahezza.mahezza.data.source.firebase.response

data class ChallengeOfContentResponse(
    val position : Int = 0,
    val challengeId : String = "",
    val instruction : String = "",
    val title : String = "",
    var completed: Boolean = false,
    val challengeNumber : Int = 0,
    val numberOfChallenges: Int = 0,
)
