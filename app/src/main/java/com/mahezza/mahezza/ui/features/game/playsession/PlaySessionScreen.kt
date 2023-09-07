package com.mahezza.mahezza.ui.features.game.playsession

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.GifImage
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.StackedPhotoProfiles
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.game.ExitGameDialog
import com.mahezza.mahezza.ui.features.game.GameEvent
import com.mahezza.mahezza.ui.features.game.GameUiState
import com.mahezza.mahezza.ui.features.game.GameViewModel
import com.mahezza.mahezza.ui.features.game.playsession.service.PlaySessionService
import com.mahezza.mahezza.ui.features.game.playsession.service.PlaySessionServiceHelper
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.Grey
import com.mahezza.mahezza.ui.theme.GreyLight
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsBold40
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsMedium20
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.RedDanger
import com.mahezza.mahezza.ui.theme.White

@Composable
fun PlaySessionScreen(
    navController: NavController,
    gameViewModel: GameViewModel,
    viewModel: PlaySessionViewModel
) {
    changeStatusBarColor(color = White)
    val context = LocalContext.current
    val uiState = viewModel.uiState.collectAsState()
    val gameUiState = gameViewModel.uiState.collectAsState()

    viewModel.setChildrenAndPuzzle(
        gameViewModel.getChildren(),
        gameViewModel.getPuzzle()
    )

    var hasPostNotifications by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )

    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasPostNotifications = granted
        }
    )
    LaunchedEffect(key1 = hasPostNotifications) {
        if (!hasPostNotifications && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    var playSessionService : PlaySessionService? by remember {
        mutableStateOf(null)
    }
    val serviceConnection = remember {
        object: ServiceConnection {
            override fun onServiceConnected(componentName: ComponentName?, binder: IBinder?) {
                val playSessionBinder = binder as PlaySessionService.PlaySessionBinder
                playSessionService = playSessionBinder.getService()
            }

            override fun onServiceDisconnected(componentName: ComponentName?) {
                playSessionService = null
            }
        }
    }
    DisposableEffect(Unit){
        Intent(context, PlaySessionService::class.java).also { intent ->
            context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        onDispose {
            PlaySessionServiceHelper.triggerForegroundService(
                context = context,
                action = PlaySessionService.Actions.Finished.name
            )
            context.unbindService(serviceConnection)
        }
    }

    LaunchedEffect(key1 = playSessionService) {
        playSessionService?.let { service ->
            service.setChildren(uiState.value.children)
            service.setPuzzle(uiState.value.puzzle)

            service.playSessionServiceUiState.collect{ playSessionServiceUiState ->
                playSessionServiceUiState.currentSong?.let { song ->
                    viewModel.onEvent(PlaySessionEvent.SetCurrentSong(song))
                }
                viewModel.onEvent(PlaySessionEvent.SetElapsedTime(playSessionServiceUiState.elapsedTime))
                viewModel.onEvent(PlaySessionEvent.SetStopwatchState(playSessionServiceUiState.stopwatchState))
                viewModel.onEvent(PlaySessionEvent.SetCurrentTrack(playSessionServiceUiState.currentTrack))
            }

        }
    }

    LaunchedEffect(key1 = gameUiState.value.acknowledgeCode){
        if (gameUiState.value.acknowledgeCode?.name == GameUiState.AcknowledgeCode.PLAY_SESSION_AND_EXIT.name) {
            navController.navigate(route = Routes.Home){
                popUpTo(Routes.Home){
                    inclusive = true
                }
            }
        }
        if (gameUiState.value.acknowledgeCode?.name == GameUiState.AcknowledgeCode.PLAY_SESSION_AND_NEXT.name) {
            navController.navigate(Routes.TakeTwibbon)
        }
        gameViewModel.onEvent(GameEvent.OnSaveGameStatusAcknowledged)
    }

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(PlaySessionEvent.OnGeneralMessageShowed)
        }
    }

    PlaySessionContent(
        uiState = uiState.value,
        onSaveGameAndExit = {
            gameViewModel.onEvent(GameEvent.SaveGame(
                elapsedTime = uiState.value.stopwatchTime,
                lastActivity = context.getString(R.string.photo_together),
                acknowledgeCode = GameUiState.AcknowledgeCode.PLAY_SESSION_AND_EXIT
            ))
        },
        onSaveGameAndNext = {
            gameViewModel.onEvent(GameEvent.SaveGame(
                elapsedTime = uiState.value.stopwatchTime,
                lastActivity = context.getString(R.string.photo_together),
                acknowledgeCode = GameUiState.AcknowledgeCode.PLAY_SESSION_AND_NEXT
            ))
        },
        onPlayPauseClick = {
            PlaySessionServiceHelper.triggerForegroundService(
                context = context,
                action = if (
                    uiState.value.stopwatchState.name == PlaySessionService.StopwatchState.Started.name
                ) {
                    PlaySessionService.Actions.PAUSE.name
                } else {
                    PlaySessionService.Actions.START.name
                }
            )
        }
    )

    LoadingScreen(isShowLoading = uiState.value.isShowLoading  || gameUiState.value.isLoading)
}

@Composable
fun PlaySessionContent(
    uiState: PlaySessionUiState,
    onSaveGameAndNext : () -> Unit,
    onSaveGameAndExit : () -> Unit,
    onPlayPauseClick : () -> Unit
) {
    val context = LocalContext.current
    var isShowExitDialog by remember {
        mutableStateOf(false)
    }
    if (isShowExitDialog){
        ExitGameDialog(
            onExit = {
                isShowExitDialog = false
                finishTheService(context)
                onSaveGameAndExit()
            },
            onDismissRequest = {
                isShowExitDialog = it
            }
        )
    }
    var song by remember { mutableStateOf(uiState.currentSong) }
    val isCurrentSongChanged = uiState.currentSong != null && song != uiState.currentSong
    if (isCurrentSongChanged) {
        song = uiState.currentSong
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ){
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth()){
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    StackedPhotoProfiles(
                        photoProfiles = uiState.children.map { it.photoUrl },
                        imageSize = 48.dp,
                        offset = (-28).dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = uiState.children.joinToString { it.name },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            style = PoppinsMedium16,
                            color = Black
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier.size(12.dp),
                                tint = AccentYellowDark,
                                painter = painterResource(id = R.drawable.ic_puzzle),
                                contentDescription = stringResource(id = R.string.play)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = R.string.play),
                                style = PoppinsRegular12,
                                color = AccentYellowDark
                            )
                        }
                    }
                }
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = RedDanger
                    ),
                    onClick = {
                        isShowExitDialog = true
                    }
                ) {
                    Text(
                        text = stringResource(id = R.string.sign_out),
                        style = PoppinsRegular12,
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Box(modifier = Modifier.wrapContentSize()){
                GifImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .clip(RoundedCornerShape(8.dp))
                        ,
                    gifUrl = uiState.puzzle?.illustrationUrl,
                    contentDescription = null
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.7f)
                        .padding(16.dp)
                ){
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        White.copy(alpha = 0.6f),
                                        White.copy(alpha = 0.1f),
                                    ),
                                    startY = 0f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )
                    Text(
                        modifier = Modifier
                            .align(Alignment.Center),
                        text = uiState.stopwatchTime,
                        style = PoppinsBold40,
                        color = Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FilledIconButton(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .align(CenterHorizontally)
                ,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = AccentYellow,
                    contentColor = Black
                ),
                onClick = onPlayPauseClick
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .offset(
                            if (uiState.isPlaying) {
                                0.dp
                            } else {
                                2.dp
                            }
                        ),
                    painter = painterResource(
                        id = if(uiState.isPlaying){
                            R.drawable.ic_pause
                        }else{
                            R.drawable.ic_play
                        }
                    ),
                    contentDescription = stringResource(id = R.string.play_or_pause),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = song?.typeOrArtist ?: "",
                style = PoppinsMedium14,
                color = GreyText
            )
            Text(
                modifier = Modifier
                    .align(CenterHorizontally),
                text = song?.title ?: "",
                style = PoppinsMedium20,
                color = Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                progress = uiState.songProgress,
                trackColor = Grey,
                color = AccentYellow,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Text(
                    modifier = Modifier
                        .align(CenterStart),
                    text = uiState.currentSongPosition,
                    style = PoppinsRegular12,
                    color = Black
                )
                Text(
                    modifier = Modifier
                        .align(CenterEnd),
                    text = uiState.currentSongDuration,
                    style = PoppinsRegular12,
                    color = Black
                )
            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(end = 8.dp)
//                    ,
//                horizontalArrangement = Arrangement.End,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(
//                    modifier = Modifier.size(16.dp),
//                    colors = IconButtonDefaults.iconButtonColors(
//                        contentColor = Black,
//                        containerColor = Color.Transparent
//                    ),
//                    onClick = {
//                        // Sound off
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.VolumeOff,
//                        contentDescription = stringResource(id = R.string.volume_off)
//                    )
//                }
//                Spacer(modifier = Modifier.width(16.dp))
//                IconButton(
//                    modifier = Modifier.size(16.dp),
//                    colors = IconButtonDefaults.iconButtonColors(
//                        contentColor = Black,
//                        containerColor = Color.Transparent
//                    ),
//                    onClick = {
//                        // Sound off
//                    }
//                ) {
//                    Icon(
//                        imageVector = Icons.Filled.Repeat,
//                        contentDescription = stringResource(id = R.string.repeat)
//                    )
//                }
//            }
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        GreyLight,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp),
                horizontalAlignment = CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                song?.lyrics?.forEach {lyric ->
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = lyric,
                        style = PoppinsRegular14,
                        color = GreyText
                    )
                }
            }
            Spacer(modifier = Modifier.height(104.dp))
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            FilledAccentYellowButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.puzzle_finished)
            ) {
                finishTheService(context)
                onSaveGameAndNext()
            }
        }
    }
}

private fun finishTheService(context : Context){
    PlaySessionServiceHelper.triggerForegroundService(
        context = context,
        action = PlaySessionService.Actions.Finished.name
    )
}

@Preview
@Composable
fun PlaySessionContentPreview() {
    PlaySessionContent(
        uiState = PlaySessionUiState(),
        onSaveGameAndExit = {},
        onSaveGameAndNext = {},
        onPlayPauseClick = {}
    )
}