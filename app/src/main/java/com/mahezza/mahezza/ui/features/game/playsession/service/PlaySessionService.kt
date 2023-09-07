package com.mahezza.mahezza.ui.features.game.playsession.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C.AUDIO_CONTENT_TYPE_MUSIC
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import coil.ImageLoader
import coil.request.ImageRequest
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.di.IODispatcher
import com.mahezza.mahezza.di.PlaySessionNotificationBuilder
import com.mahezza.mahezza.ui.features.game.playsession.PlaySessionUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Timer
import javax.inject.Inject
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class PlaySessionService : Service() {

    companion object {
        const val NOTIFICATION_ID = 1
        const val StopwatchState = "stopwatch_state"
    }

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    @IODispatcher
    lateinit var dispatcher: CoroutineDispatcher

    @Inject
    @PlaySessionNotificationBuilder
    lateinit var playSessionNotificationBuilder : NotificationCompat.Builder

    private val binder = PlaySessionBinder()
    private var duration : Duration = Duration.ZERO
    private lateinit var timer: Timer

    private var exoPlayer : ExoPlayer? = null

    private var _playSessionServiceUiState = MutableStateFlow(PlaySessionServiceUiState())
    val playSessionServiceUiState : StateFlow<PlaySessionServiceUiState>
        get() = _playSessionServiceUiState.asStateFlow()

    override fun onBind(intent : Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        exoPlayer = ExoPlayer
            .Builder(this)
            .setWakeMode(PowerManager.PARTIAL_WAKE_LOCK)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AUDIO_CONTENT_TYPE_MUSIC)
                    .build(),
                true
            )
            .build()
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when(playbackState){
                    Player.STATE_ENDED -> setNextSong()
                }
            }
        })
    }

    override fun onDestroy() {
        exoPlayer?.release()
        exoPlayer = null
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.name -> {
                setPauseButton()
                startForegroundService()
                startStopwatch { hours, minutes, seconds ->
                    val elapsedTime = formatTime(seconds, minutes, hours)
                    _playSessionServiceUiState.update { it.copy(elapsedTime = elapsedTime) }
                    updateCurrentTrack()
                    updateNotification(hours = hours, minutes = minutes, seconds = seconds)
                }
                startSong()
            }
            Actions.PAUSE.name -> {
                stopStopwatch()
                pauseSong()
                setResumeButton()
            }
            Actions.Finished.name -> {
                stopStopwatch()
                stopSong()
                cancelStopwatch()
                stopForegroundService()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startSong() {
        if (playSessionServiceUiState.value.currentSong == null){
            setNextSong()
            return
        }
        exoPlayer?.prepare()
        exoPlayer?.play()
    }

    private fun setNextSong(){
        val nextIndex = getNextSongIndex()
        val nextSong = playSessionServiceUiState.value.getSong(nextIndex) ?: return
        _playSessionServiceUiState.update {
            it.copy(
                currentIndexSong = nextIndex,
                currentSong = nextSong
            )
        }
        val mediaItem = MediaItem.fromUri(nextSong.songUrl)
        exoPlayer?.setMediaItem(mediaItem)
        startSong()
    }

    private fun getNextSongIndex() : Int {
        var index = playSessionServiceUiState.value.currentIndexSong
        index++
        if (isSongIndexOutOfBound(index)){
            index = 0
        }
        return index
    }

    private fun isSongIndexOutOfBound(index : Int) : Boolean {
        return index >= playSessionServiceUiState.value.getSongsSize() || index < 0
    }

    private fun pauseSong(){
        exoPlayer?.pause()
    }

    private fun stopSong() {
        exoPlayer?.stop()
    }

    private fun startStopwatch(onTick: (h: String, m: String, s: String) -> Unit) {
        _playSessionServiceUiState.update {
            it.copy(
                stopwatchState = PlaySessionService.StopwatchState.Started
            )
        }
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            duration.toComponents { hours, minutes, seconds, _ ->
                onTick(hours.toInt().pad(), minutes.pad(), seconds.pad())
            }

        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        _playSessionServiceUiState.update {
            it.copy(
                stopwatchState = PlaySessionService.StopwatchState.Paused
            )
        }
    }

    private fun cancelStopwatch() {
        duration = Duration.ZERO
        _playSessionServiceUiState.update {
            it.copy(
                stopwatchState = PlaySessionService.StopwatchState.Idle
            )
        }
    }

    private fun updateCurrentTrack() {
        CoroutineScope(Dispatchers.Main).launch{
            val currentPosition = exoPlayer?.currentPosition ?: 0
            val songDuration = exoPlayer?.duration ?: 0
            _playSessionServiceUiState.update {
                it.copy(
                    currentTrack =  PlaySessionUiState.Track(currentPosition, songDuration)
                )
            }
        }
    }

    private fun startForegroundService() {
        startForeground(NOTIFICATION_ID, playSessionNotificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(NOTIFICATION_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        }
        stopSelf()
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        playSessionNotificationBuilder.apply {
            setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(playSessionServiceUiState.value.bannerBitmap)
            )
            setContentTitle(
                playSessionServiceUiState.value.getGameTitle()
            )
        }
        notificationManager.notify(
            NOTIFICATION_ID,
            playSessionNotificationBuilder.setContentText(
                getString(R.string.elapsed_time, formatTime(
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds,
                ))
            ).build()
        )
    }

    private fun setPauseButton() {
        playSessionNotificationBuilder.clearActions()
        playSessionNotificationBuilder.addAction(PlaySessionServiceHelper.getPauseNotificationAction(this))
        notificationManager.notify(NOTIFICATION_ID, playSessionNotificationBuilder.build())
    }

    private fun setResumeButton() {
        playSessionNotificationBuilder.clearActions()
        playSessionNotificationBuilder.addAction(PlaySessionServiceHelper.getResumeNotificationAction(this))
        notificationManager.notify(NOTIFICATION_ID, playSessionNotificationBuilder.build())
    }

    private fun formatTime(seconds: String, minutes: String, hours: String): String {
        return "$hours:$minutes:$seconds"
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }

    fun setChildren(children: List<Child>) {
        _playSessionServiceUiState.update {
            it.copy(children = children)
        }
    }

    fun setPuzzle(puzzle: Puzzle?) {
        val imageLoader = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(puzzle?.banner)
            .target(
                onSuccess = { result ->
                    val bannerBitmap = result.toBitmap(width = 1600, height = 900)
                    _playSessionServiceUiState.update {
                        it.copy(
                            bannerBitmap = bannerBitmap,
                        )
                    }
                }
            )
            .build()
        imageLoader.enqueue(request)
        _playSessionServiceUiState.update {
            it.copy(
                puzzle = puzzle,
            )
        }
    }

    inner class PlaySessionBinder : Binder() {
        fun getService(): PlaySessionService = this@PlaySessionService
    }

    enum class Actions {
        START, PAUSE, Finished
    }

    enum class StopwatchState {
        Idle,
        Started,
        Paused,
        Finished
    }
}