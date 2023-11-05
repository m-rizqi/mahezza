package com.mahezza.mahezza.data.source.firebase.response

data class GameResponse(
    val id : String = "",
    val parentId : String = "",
    val childrenIds : List<String> = emptyList(),
    val puzzleId : String = "",
    val status : String = "",
    val lastActivity : String = "",
    val elapsedTime : String = "",
    val twibbonUrl : String? = null,
    val course: CourseResponse? = null
)