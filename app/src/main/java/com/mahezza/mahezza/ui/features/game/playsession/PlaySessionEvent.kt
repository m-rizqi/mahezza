package com.mahezza.mahezza.ui.features.game.playsession

import com.mahezza.mahezza.data.model.Song
import com.mahezza.mahezza.ui.features.game.playsession.service.PlaySessionService

sealed class PlaySessionEvent {
    object OnGeneralMessageShowed : PlaySessionEvent()
    object OnPlayPauseClick : PlaySessionEvent()
    class SetElapsedTime(val time : String) : PlaySessionEvent()
    class SetStopwatchState(val state : PlaySessionService.StopwatchState) : PlaySessionEvent()
    class SetCurrentSong(val song: Song) : PlaySessionEvent()
    class SetCurrentTrack(val track : PlaySessionUiState.Track) : PlaySessionEvent()
}
