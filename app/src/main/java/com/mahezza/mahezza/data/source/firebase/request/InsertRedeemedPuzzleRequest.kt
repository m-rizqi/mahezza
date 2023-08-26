package com.mahezza.mahezza.data.source.firebase.request

import com.google.firebase.Timestamp

data class InsertRedeemedPuzzleRequest(
    val puzzleId : String,
    val timestamp : Timestamp
)