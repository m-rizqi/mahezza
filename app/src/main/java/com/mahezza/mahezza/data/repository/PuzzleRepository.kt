package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse
import kotlinx.coroutines.flow.Flow

interface PuzzleRepository {
    suspend fun getPuzzleByQRCode(qrcode : String) : Result<Puzzle>

    fun getPuzzleByIds(ids : List<String>) : Flow<Result<List<Puzzle>>>
}