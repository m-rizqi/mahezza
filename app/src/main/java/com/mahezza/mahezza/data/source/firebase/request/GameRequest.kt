package com.mahezza.mahezza.data.source.firebase.request

data class GameRequest(
    val id : String,
    val parentId : String,
    val childrenIds : List<String>,
    val puzzleId : String,
    val status : String,
    val lastActivity : String,
    val elapsedTime : String,
    val twibbonUrl : String?,
    val course: CourseRequest?
)
