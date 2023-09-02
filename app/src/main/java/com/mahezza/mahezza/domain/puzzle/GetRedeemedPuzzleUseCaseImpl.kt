package com.mahezza.mahezza.domain.puzzle

import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.repository.PuzzleRepository
import com.mahezza.mahezza.data.repository.UserRepository
import com.mahezza.mahezza.data.source.datastore.MahezzaDataStore
import com.mahezza.mahezza.domain.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import timber.log.Timber
import javax.inject.Inject

class GetRedeemedPuzzleUseCaseImpl @Inject constructor(
    private val dataStore: MahezzaDataStore,
    private val userRepository: UserRepository,
    private val puzzleRepository: PuzzleRepository
): GetRedeemedPuzzleUseCase {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun invoke(): Flow<Result<List<Puzzle>>> {
        return dataStore.firebaseUserIdPreference
            .filterNotNull()
            .flatMapLatest { userId ->
                userRepository.getRedeemedPuzzles(userId)
            }
            .flatMapLatest { resultOfRedeemedPuzzles ->
                when (resultOfRedeemedPuzzles) {
                    is com.mahezza.mahezza.data.Result.Fail -> flowOf(com.mahezza.mahezza.data.Result.Fail(resultOfRedeemedPuzzles.message))
                    is com.mahezza.mahezza.data.Result.Success -> {
                        if (resultOfRedeemedPuzzles.data == null) {
                            flowOf(com.mahezza.mahezza.data.Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again)))
                        } else {
                            val puzzleIds = resultOfRedeemedPuzzles.data.map { it.puzzleId }
                            return@flatMapLatest puzzleRepository.getPuzzleByIds(puzzleIds)
                        }
                    }
                }
            }
            .mapLatest { resultOfPuzzle ->
                when (resultOfPuzzle) {
                    is com.mahezza.mahezza.data.Result.Fail -> Result.Fail(resultOfPuzzle.message)
                    is com.mahezza.mahezza.data.Result.Success -> {
                        if (resultOfPuzzle.data == null) {
                            Result.Fail(StringResource.StringResWithParams(R.string.problem_occur_try_again))
                        } else {
                            Result.Success(resultOfPuzzle.data)
                        }
                    }
                }
            }
    }

}