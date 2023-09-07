package com.mahezza.mahezza.ui.features.game.playsession

import androidx.lifecycle.ViewModel
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.Song
import com.mahezza.mahezza.ui.features.game.playsession.service.PlaySessionService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlaySessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlaySessionUiState())
    val uiState : StateFlow<PlaySessionUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event: PlaySessionEvent){
        when(event){
            PlaySessionEvent.OnGeneralMessageShowed -> _uiState.update { it.copy(generalMessage = null) }
            PlaySessionEvent.OnPlayPauseClick -> playOrPause()
            is PlaySessionEvent.SetElapsedTime -> setStopwatchTime(event.time)
            is PlaySessionEvent.SetStopwatchState -> setStopwatchState(event.state)
            is PlaySessionEvent.SetCurrentSong -> setSong(event.song)
            is PlaySessionEvent.SetCurrentTrack -> setCurrentTrack(event.track)
        }
    }

    private fun setSong(song: Song) {
        _uiState.update { it.copy(currentSong = song) }
    }

    private fun setStopwatchTime(time: String) {
        _uiState.update { it.copy(stopwatchTime = time) }
    }

    private fun setCurrentTrack(track: PlaySessionUiState.Track) {
        val currentPosition = formatTime(track.currentPosition)
        val duration = formatTime(track.songDuration)
        val sliderValue = try {
            track.currentPosition.toFloat() / track.songDuration.toFloat()
        } catch (e: Exception){ 0f }
        _uiState.update {
            it.copy(
                currentTrack = track,
                currentSongPosition = currentPosition,
                currentSongDuration = duration,
                songProgress = sliderValue
            )
        }
    }

    private fun formatTime(durationInMillis: Long): String {
        val totalSeconds = durationInMillis / 1000
        val hours = totalSeconds / 3600
        val remainingSeconds = totalSeconds % 3600
        val minutes = remainingSeconds / 60
        val seconds = remainingSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun setStopwatchState(state: PlaySessionService.StopwatchState) {
        _uiState.update {
            it.copy(
                stopwatchState = state,
                isPlaying = state == PlaySessionService.StopwatchState.Started
            )
        }
    }

    private fun playOrPause() {
        _uiState.update {
            it.copy(
                isPlaying = uiState.value.isPlaying.not()
            )
        }
    }

    fun setChildrenAndPuzzle(children : List<Child>, puzzle: Puzzle?){
        _uiState.update {
            it.copy(
                children = children,
                puzzle = puzzle,
                currentSong = null,
                generalMessage = if (puzzle == null) StringResource.StringResWithParams(R.string.you_dont_choose_puzzle) else null
            )
        }
    }
}