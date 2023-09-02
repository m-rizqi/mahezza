package com.mahezza.mahezza.data.model

import com.mahezza.mahezza.data.source.firebase.response.LyricResponse

data class Lyric(
    val timestamp : String,
    val lyric : String
)

fun LyricResponse.toLyric() = Lyric(
    timestamp = this.timestamp,
    lyric = this.lyric
)