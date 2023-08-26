package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse

data class Puzzle(
    val id : String,
    val name : String,
    val banner : String
)

fun PuzzleResponse.toPuzzle() : Puzzle{
    return Puzzle(
        id = this.id,
        name = this.name,
        banner = this.banner
    )
}
