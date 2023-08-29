package com.mahezza.mahezza.data.source.firebase.response

import com.google.firebase.Timestamp

data class RedeemedPuzzleResponse(
    val puzzleId : String = "",
    val timestamp : Timestamp = Timestamp.now()
)
