package com.mahezza.mahezza.data.source.firebase.firestore

import com.mahezza.mahezza.data.source.firebase.FirebaseResult
import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse

interface PuzzleFirebaseFirestore {

    companion object {
        const val PUZZLE_PATH = "puzzles"
        const val QR_CODE_PATH = "qrcodes"
    }

    suspend fun findPuzzleIdByQRCode(qrcode : String) : FirebaseResult<PuzzleResponse>

    suspend fun getPuzzleById(id : String) : FirebaseResult<PuzzleResponse>
}