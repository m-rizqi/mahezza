package com.mahezza.mahezza.domain.puzzle

import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.domain.Result
import kotlinx.coroutines.flow.Flow

interface GetRedeemedPuzzleUseCase {
    operator fun invoke() : Flow<Result<List<Puzzle>>>
}