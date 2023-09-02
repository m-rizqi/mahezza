package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse
import com.mahezza.mahezza.data.source.firebase.response.SongResponse
import kotlinx.coroutines.flow.Flow

interface PuzzleFirebaseFirestore {

    companion object {
        const val PUZZLE_PATH = "puzzles"
        const val QR_CODE_PATH = "qrcodes"
        const val SONGS = "songs"
    }

    suspend fun findPuzzleIdByQRCode(qrcode : String) : FirebaseResult<PuzzleResponse>

    suspend fun getPuzzleById(id : String) : FirebaseResult<PuzzleResponse>

    fun getPuzzleByIds(ids : List<String>) : Flow<FirebaseResult<out List<PuzzleResponse>>>

    fun getSongs(puzzleId : String) : Flow<FirebaseResult<out List<SongResponse>>>
}