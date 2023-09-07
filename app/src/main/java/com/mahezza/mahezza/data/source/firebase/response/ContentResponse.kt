package com.mahezza.mahezza.data.source.firebase.response

data class ContentResponse(
    val challenge: ChallengeOfContentResponse? = null,
    val image : ImageOfContentResponse? = null,
    val video : VideoOfContentResponse? = null,
    val script : ScriptOfContentResponse? = null
)