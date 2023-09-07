package com.mahezza.mahezza.ui.features.game.playsession

import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.Song
import com.mahezza.mahezza.ui.features.game.playsession.service.PlaySessionService

data class PlaySessionUiState(

    val isPlaying : Boolean = false,
    var stopwatchTime : String = "00:00:00",
    val stopwatchState: PlaySessionService.StopwatchState = PlaySessionService.StopwatchState.Idle,

    val currentSong : Song? = null,
    val currentSongPosition : String = "00:00",
    val currentSongDuration : String = "00:00",
    val currentTrack: Track = Track(0,0),
    val songProgress: Float = 0f,

    val children : List<Child> = listOf(),
    val puzzle: Puzzle? = null,

    val isShowLoading : Boolean = false,
    val generalMessage : StringResource? = null,
) {

    data class Track(
        val currentPosition : Long = 0,
        val songDuration : Long = 0
    )

}
