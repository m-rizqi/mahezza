package com.mahezza.mahezza.data.source.firebase.response

data class PuzzleResponse(
    val id : String = "",
    val name : String = "",
    val banner : String = "",
    val description : String = "",
    val illustrationUrl : String = "",
    val songs : List<SongResponse> = emptyList(),
    val twibbonUrl : String = ""
)
