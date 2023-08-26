package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.toPuzzle
import com.mahezza.mahezza.data.source.firebase.firestore.PuzzleFirebaseFirestore
import javax.inject.Inject

class MainPuzzleRepository @Inject constructor(
    private val puzzleFirebaseFirestore: PuzzleFirebaseFirestore,
) : PuzzleRepository {
    override suspend fun getPuzzleByQRCode(qrcode: String): Result<Puzzle> {
        val firebaseResult = puzzleFirebaseFirestore.findPuzzleIdByQRCode(qrcode)
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!.toPuzzle())
        return Result.Fail(firebaseResult.message)
    }
}