package com.mahezza.mahezza.domain.puzzle

import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.domain.Result

interface RedeemPuzzleUseCase {
    suspend operator fun invoke(qrcode : String) : Result<Puzzle>
}