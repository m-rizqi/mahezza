package com.mahezza.mahezza.data.source.firebase.response

data class SongResponse(
    val id : String = "",
    val lyrics : List<String> = emptyList(),
    val songUrl : String = "",
    val title : String = "",
    val typeOrArtist : String = ""
)
