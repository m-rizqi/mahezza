package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Puzzle

interface PuzzleRepository {
    suspend fun getPuzzleByQRCode(qrcode : String) : Result<Puzzle>
}