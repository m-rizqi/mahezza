package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.response.SongResponse

data class Song(
    val id : String,
    val lyrics : List<String>,
    val songUrl : String,
    val title : String,
    val typeOrArtist : String
)

fun SongResponse.toSong() = Song(
    id = this.id,
    lyrics = this.lyrics,
    songUrl = this.songUrl,
    title = this.title,
    typeOrArtist = this.typeOrArtist
)
