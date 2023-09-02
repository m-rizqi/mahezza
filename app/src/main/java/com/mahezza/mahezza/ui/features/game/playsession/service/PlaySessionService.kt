package com.mahezza.mahezza.ui.features.game.playsession.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.Song
import com.mahezza.mahezza.di.PlaySessionNotificationBuilder
import com.mahezza.mahezza.ui.features.game.playsession.PlaySessionUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
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
    @PlaySessionNotificationBuilder
    lateinit var playSessionNotificationBuilder : NotificationCompat.Builder

    private val binder = PlaySessionBinder()
    private var duration : Duration = Duration.ZERO
    private lateinit var timer: Timer

    private val _seconds = MutableStateFlow("00")
    val seconds: StateFlow<String> = _seconds.asStateFlow()

    private val _minutes = MutableStateFlow("00")
    val minutes: StateFlow<String> = _minutes.asStateFlow()

    private val _hours = MutableStateFlow("00")
    val hours: StateFlow<String> = _hours.asStateFlow()

    private val _time = MutableStateFlow("00:00:00")
    val time: StateFlow<String> = _time.asStateFlow()

    private val _currentStopwatchState = MutableStateFlow(PlaySessionService.StopwatchState.Idle)
    val currentStopwatchState: StateFlow<PlaySessionService.StopwatchState> = _currentStopwatchState.asStateFlow()

    private var children : List<Child> = emptyList()
    private var puzzle : Puzzle? = null
    private var currentIndexSong : Int = 0
    private val _currentSong = MutableStateFlow<Song?>(null)
    val currentSong : StateFlow<Song?>
        get() = _currentSong.asStateFlow()
    private var puzzleBannerBitmap : Bitmap? = null

    private var mediaPlayer: MediaPlayer? = null
    private val _currentTrack = MutableStateFlow(PlaySessionUiState.Track(0, 0))
    val currentTrack : StateFlow<PlaySessionUiState.Track>
        get() = _currentTrack.asStateFlow()

    override fun onBind(intent : Intent?): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setOnCompletionListener {
            playNextSong()
        }
        mediaPlayer?.setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            Actions.START.name -> {
                setPauseButton()
                startForegroundService()
                startStopwatch { hours, minutes, seconds ->
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
        if (mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    private fun playNextSong(){
        currentIndexSong++
        if (currentIndexSong >= (puzzle?.songs?.size ?: 1)) {
            currentIndexSong = 0
        }
        val nextSong = puzzle?.songs?.get(currentIndexSong)
        updateSong(nextSong)
        startSong()
    }

    private fun updateSong(song: Song?){
        _currentSong.update { song }
        currentSong.value?.songUrl?.let { datasource ->
            mediaPlayer?.setDataSource(datasource)
            mediaPlayer?.prepare()
        }
    }

    private fun pauseSong(){
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
        }
    }

    private fun stopSong() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
    }

    private fun startStopwatch(onTick: (h: String, m: String, s: String) -> Unit) {
        _currentStopwatchState.update {
            PlaySessionService.StopwatchState.Started
        }
        timer = fixedRateTimer(initialDelay = 1000L, period = 1000L) {
            duration = duration.plus(1.seconds)
            updateCurrentTrack()
            updateTimeUnits()
            onTick(hours.value, minutes.value, seconds.value)
        }
    }

    private fun stopStopwatch() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        _currentStopwatchState.update {
            PlaySessionService.StopwatchState.Paused
        }
    }

    private fun cancelStopwatch() {
        duration = Duration.ZERO
        _currentStopwatchState.update {
            PlaySessionService.StopwatchState.Idle
        }
        updateTimeUnits()
    }

    private fun updateTimeUnits() {
        duration.toComponents { hours, minutes, seconds, _ ->
            _hours.update { hours.toInt().pad() }
            _minutes.update { minutes.pad() }
            _seconds.update { seconds.pad() }
            _time.update { formatTime(seconds.pad(), minutes.pad(), hours.toInt().pad()) }
        }
    }

    private fun updateCurrentTrack() {
        val currentPosition = mediaPlayer?.currentPosition ?: 0
        val songDuration = mediaPlayer?.duration ?: 0
        _currentTrack.update {
            PlaySessionUiState.Track(currentPosition, songDuration)
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
                    .bigPicture(puzzleBannerBitmap)
            )
            setContentTitle(
                "${children.joinToString { it.name }} - ${puzzle?.name}"
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
        this.children = children
    }

    fun setPuzzle(puzzle: Puzzle?) {
        this.puzzle = puzzle

        val imageLoader = ImageLoader(this)
        val request = ImageRequest.Builder(this)
            .data(puzzle?.banner)
            .target(
                onSuccess = { result ->
                    puzzleBannerBitmap = result.toBitmap(width = 1600, height = 900)
                }
            )
            .build()
        imageLoader.enqueue(request)
        updateSong(puzzle?.songs?.get(currentIndexSong))
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