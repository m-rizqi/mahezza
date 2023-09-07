package com.mahezza.mahezza.ui.features.game.playsession.service

import android.graphics.Bitmap
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.Song
import com.mahezza.mahezza.ui.features.game.playsession.PlaySessionUiState

data class PlaySessionServiceUiState(
    val elapsedTime : String = "00:00:00",
    val stopwatchState: PlaySessionService.StopwatchState = PlaySessionService.StopwatchState.Idle,
    val currentSong: Song? = null,
    val currentTrack : PlaySessionUiState.Track = PlaySessionUiState.Track(),
    val puzzle: Puzzle? = null,
    val children : List<Child> = emptyList(),
    val currentIndexSong : Int = -1,
    val bannerBitmap : Bitmap? = null
) {
    fun getSongsSize() : Int = (puzzle?.songs?.size ?: 0)
    fun getSong(index : Int) : Song? = puzzle?.songs?.getOrNull(index)
    fun getGameTitle() : String = "${children.joinToString { it.name }} - ${puzzle?.name}"
}