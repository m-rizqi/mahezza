package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse

data class Puzzle(
    val id : String,
    val name : String,
    val banner : String,
    val description : String,
    val illustrationUrl : String,
    val songs : List<Song>,
    val twibbonUrl : String
)

fun PuzzleResponse.toPuzzle() : Puzzle{
    return Puzzle(
        id = this.id,
        name = this.name,
        banner = this.banner,
        description = this.description,
        illustrationUrl = this.illustrationUrl,
        songs = this.songs.map { it.toSong() },
        twibbonUrl = this.twibbonUrl
    )
}
