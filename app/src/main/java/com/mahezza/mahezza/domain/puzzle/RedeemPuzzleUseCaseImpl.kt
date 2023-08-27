package com.mahezza.mahezza.domain.puzzle

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.repository.PuzzleRepository
import com.mahezza.mahezza.data.repository.UserRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.data.source.firebase.request.InsertRedeemedPuzzleRequest
import com.mahezza.mahezza.domain.Result
import com.mahezza.mahezza.domain.common.LocalDateTimeAndTimestampConverter
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import javax.inject.Inject

class RedeemPuzzleUseCaseImpl @Inject constructor(
    private val puzzleRepository: PuzzleRepository,
    private val userRepository: UserRepository,
    private val dataStore: MahezzaDataStore
) : RedeemPuzzleUseCase {

    private val localDateTimeAndTimestampConverter = LocalDateTimeAndTimestampConverter()

    override suspend fun invoke(qrcode: String): Result<Puzzle> {
        val getPuzzleByQRCodeResult = puzzleRepository.getPuzzleByQRCode(qrcode)
        if (getPuzzleByQRCodeResult is com.mahezza.mahezza.data.Result.Fail) return Result.Fail(getPuzzleByQRCodeResult.message)
        if (getPuzzleByQRCodeResult.data == null) return Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))

        val userId = dataStore.firebaseUserIdPreference.first() ?: return Result.Fail(StringResource.StringResWithParams(R.string.user_id_is_not_found))
        val puzzle = getPuzzleByQRCodeResult.data
        val insertRedeemedPuzzleRequest = InsertRedeemedPuzzleRequest(
            puzzleId = puzzle.id,
            timestamp = localDateTimeAndTimestampConverter.convertLocalDateTimeToTimestamp(LocalDateTime.now())
        )
        val insertPuzzleResult = userRepository.insertRedeemedPuzzle(userId, insertRedeemedPuzzleRequest)

        return when(insertPuzzleResult){
            is com.mahezza.mahezza.data.Result.Fail -> Result.Fail(insertPuzzleResult.message)
            is com.mahezza.mahezza.data.Result.Success -> Result.Success(getPuzzleByQRCodeResult.data)
        }
    }
}