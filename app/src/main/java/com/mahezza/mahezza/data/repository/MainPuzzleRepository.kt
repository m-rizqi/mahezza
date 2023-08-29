package com.mahezza.mahezza.data.repository

import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.toChild
import com.mahezza.mahezza.data.model.toPuzzle
import com.mahezza.mahezza.data.source.firebase.firestore.PuzzleFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MainPuzzleRepository @Inject constructor(
    private val puzzleFirebaseFirestore: PuzzleFirebaseFirestore,
) : PuzzleRepository {
    override suspend fun getPuzzleByQRCode(qrcode: String): Result<Puzzle> {
        val firebaseResult = puzzleFirebaseFirestore.findPuzzleIdByQRCode(qrcode)
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!.toPuzzle())
        return Result.Fail(firebaseResult.message)
    }

    override fun getPuzzleByIds(ids: List<String>): Flow<Result<List<Puzzle>>> {
        return puzzleFirebaseFirestore.getPuzzleByIds(ids).map {firebaseResult ->
            if (isFirebaseResultSuccess(firebaseResult)) return@map Result.Success(firebaseResult.data!!.map { puzzleResponse -> puzzleResponse.toPuzzle() })
            else Result.Fail(firebaseResult.message)
        }
    }
}