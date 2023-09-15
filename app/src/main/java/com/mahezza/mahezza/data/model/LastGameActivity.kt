package com.mahezza.mahezza.data.model

data class LastGameActivity(
    val id : String,
    val parentId : String,
    val children : List<Child>,
    val puzzle: Puzzle,
    val status : Game.Status,
    val lastActivity : String,
    val elapsedTime : String,
)
