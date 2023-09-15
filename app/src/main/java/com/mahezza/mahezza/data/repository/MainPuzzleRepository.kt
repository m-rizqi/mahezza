package com.mahezza.mahezza.data.repository

import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.mahezza.mahezza.data.Result
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.toChild
import com.mahezza.mahezza.data.model.toPuzzle
import com.mahezza.mahezza.data.model.toSong
import com.mahezza.mahezza.data.source.firebase.firestore.PuzzleFirebaseFirestore
import com.mahezza.mahezza.data.source.firebase.response.PuzzleResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class MainPuzzleRepository @Inject constructor(
    private val puzzleFirebaseFirestore: PuzzleFirebaseFirestore,
) : PuzzleRepository {
    override suspend fun getPuzzleByQRCode(qrcode: String): Result<Puzzle> {
        val firebaseResult = puzzleFirebaseFirestore.findPuzzleIdByQRCode(qrcode)
        if (isFirebaseResultSuccess(firebaseResult)) return Result.Success(firebaseResult.data!!.toPuzzle())
        return Result.Fail(firebaseResult.message)
    }

    override suspend fun getPuzzleById(id: String): Result<Puzzle> {
        val firebaseResult = puzzleFirebaseFirestore.getPuzzleById(id)
        if (isFirebaseResultSuccess(firebaseResult)) {
            var puzzle = firebaseResult.data!!.toPuzzle()
            val songsFirebase = puzzleFirebaseFirestore.getSongs(puzzle.id).first()
            val songs = if (isFirebaseResultSuccess(songsFirebase)){
                songsFirebase.data?.map { it.toSong() } ?: emptyList()
            } else {
                emptyList()
            }
            puzzle = puzzle.copy(songs = songs)
            return Result.Success(puzzle)
        }
        return Result.Fail(firebaseResult.message)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPuzzleByIds(ids: List<String>): Flow<Result<List<Puzzle>>> {
        return puzzleFirebaseFirestore.getPuzzleByIds(ids)
            .map { puzzleFirebaseResult ->
                if (isFirebaseResultSuccess(puzzleFirebaseResult)) {
                    val puzzles = puzzleFirebaseResult.data?.map { puzzleResponse -> puzzleResponse.toPuzzle() } ?: emptyList()
                    return@map Result.Success(puzzles)
                } else {
                    return@map Result.Fail<List<Puzzle>>(puzzleFirebaseResult.message)
                }
            }
            .flatMapConcat { puzzleResult ->
                when (puzzleResult) {
                    is Result.Fail -> flowOf(Result.Fail<List<Puzzle>>(puzzleResult.message))
                    is Result.Success -> {
                        val puzzleWithSongsFlow = puzzleResult.data?.map { puzzle ->
                            puzzleFirebaseFirestore.getSongs(puzzle.id)
                        } ?: emptyList()

                        combine(puzzleWithSongsFlow) { songResults ->
                            val puzzlesWithSongs = mutableListOf<Puzzle>()

                            for (i in songResults.indices) {
                                val songFirebaseResult = songResults[i]
                                if (isFirebaseResultSuccess(songFirebaseResult)) {
                                    val puzzle = puzzleResult.data?.get(i)
                                    val puzzleWithSongs = puzzle?.copy(songs = songFirebaseResult.data?.map { it.toSong() } ?: emptyList())
                                    if (puzzleWithSongs != null) {
                                        puzzlesWithSongs.add(puzzleWithSongs)
                                    }
                                } else {
                                    return@combine Result.Fail(songFirebaseResult.message)
                                }
                            }

                            Result.Success(puzzlesWithSongs)
                        }
                    }
                }
            }
    }

}