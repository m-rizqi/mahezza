package com.mahezza.mahezza.data.source.firebase.request

data class ContentRequest(
    val challenge: ChallengeOfContentRequest? = null,
    val image : ImageOfContentRequest? = null,
    val video : VideoOfContentRequest? = null,
    val script : ScriptOfContentRequest? = null
)