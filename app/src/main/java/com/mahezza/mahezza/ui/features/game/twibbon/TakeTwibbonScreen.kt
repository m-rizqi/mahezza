package com.mahezza.mahezza.ui.features.game.twibbon

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.saveBitmapToCache
import com.mahezza.mahezza.data.model.Game
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.StackedPhotoProfiles
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.getUnGrantedPermissions
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.game.ExitGameDialog
import com.mahezza.mahezza.ui.features.game.GameEvent
import com.mahezza.mahezza.ui.features.game.GameUiState
import com.mahezza.mahezza.ui.features.game.GameViewModel
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.RedDanger
import com.mahezza.mahezza.ui.theme.White
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.rememberScreenshotState

@Composable
fun TakeTwibbonScreen(
    navController: NavController,
    gameViewModel: GameViewModel,
    viewModel: TakeTwibbonViewModel
) {
    changeStatusBarColor(color = White)
    val gameUiState = gameViewModel.uiState.collectAsState()
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    val unGrantedPermissions = context.getUnGrantedPermissions(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.POST_NOTIFICATIONS,
        )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { _ -> }
    )
    LaunchedEffect(key1 = unGrantedPermissions) {
        if (unGrantedPermissions.isNotEmpty()){
            launcher.launch(unGrantedPermissions.toTypedArray())
        }
    }

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(TakeTwibbonEvent.OnGeneralMessageShowed)
        }
    }

    LaunchedEffect(key1 = gameUiState.value.acknowledgeCode){
        val imageFileName = "${gameUiState.value.puzzle?.name}-${gameUiState.value.children.joinToString { it.name }}.jpg"
        if (gameUiState.value.acknowledgeCode?.name == GameUiState.AcknowledgeCode.TWIBBON_DOWNLOAD.name){
            val request = DownloadManager.Request(Uri.parse(gameUiState.value.twibbonUrl))
                .setTitle(context.getString(R.string.app_name))
                .setDescription(context.getString(R.string.downloading_image))
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_PICTURES,
                    "${imageFileName}.jpg"
                )
            downloadManager.enqueue(request)
        }
        if (gameUiState.value.acknowledgeCode?.name == GameUiState.AcknowledgeCode.TWIBBON_SHARE.name){
            val imageUri = context.saveBitmapToCache(gameUiState.value.twibbon, imageFileName)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_twibbon_mahezza)))
        }
        if (gameUiState.value.acknowledgeCode?.name == GameUiState.AcknowledgeCode.TWIBBON_AND_EXIT.name){
            navController.navigate(route = Routes.Home){
                popUpTo(Routes.Home){
                    inclusive = true
                }
            }
        }
        if (gameUiState.value.acknowledgeCode?.name == GameUiState.AcknowledgeCode.TWIBBON_AND_NEXT.name){
            navController.navigate(Routes.Course)
            gameViewModel.onEvent(GameEvent.OnClearBitmapResource)
        }
        gameViewModel.onEvent(GameEvent.OnSaveGameStatusAcknowledged)
    }

    TakeTwibbonContent(
        gameUiState = gameUiState.value,
        uiState = uiState.value,
        onEvent = viewModel::onEvent,
        onSaveTwibbon = {bitmap, acknowledgeCode, status, lastActivity ->
            if (bitmap == null){
                showToast(context, context.getString(R.string.take_photo_first))
            } else {
                    gameViewModel.onEvent(GameEvent.SaveGame(
                        twibbon = bitmap,
                        lastActivity = lastActivity,
                        acknowledgeCode = acknowledgeCode,
                        status = status
                    )
                )
            }
        },
    )

    LoadingScreen(isShowLoading = uiState.value.isShowLoading || gameUiState.value.isLoading)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TakeTwibbonContent(
    gameUiState: GameUiState,
    uiState: TakeTwibbonUiState,
    onEvent: (TakeTwibbonEvent) -> Unit,
    onSaveTwibbon : (Bitmap?, GameUiState.AcknowledgeCode, Game.Status, String) -> Unit
) {
    val context = LocalContext.current
    val uri = context.saveBitmapToCache(null, "${System.currentTimeMillis()}")
    val cameraLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()){ success ->
        if (success && uri != null){
            onEvent(TakeTwibbonEvent.SetPhotoUri(uri))
        }
    }
    val screenshotState = rememberScreenshotState()
    var isShowExitDialog by remember {
        mutableStateOf(false)
    }
    if (isShowExitDialog){
        ExitGameDialog(
            onExit = {
                isShowExitDialog = false
                screenshotState.capture()
                screenshotState.capture()
                onSaveTwibbon(screenshotState.bitmap, GameUiState.AcknowledgeCode.TWIBBON_AND_EXIT, Game.Status.TakeTwibbon, context.getString(R.string.photo_together))
            },
            onDismissRequest = {
                isShowExitDialog = it
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ){
                Row(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    StackedPhotoProfiles(
                        photoProfiles = gameUiState.children.map { it.photoUrl },
                        imageSize = 48.dp,
                        offset = (-28).dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = gameUiState.children.joinToString { it.name },
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
                                painter = painterResource(id = R.drawable.ic_camera_retro),
                                contentDescription = stringResource(id = R.string.photo_together)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = R.string.photo_together),
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
            ScreenshotBox(
                modifier = Modifier.fillMaxWidth(),
                screenshotState = screenshotState
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ){
                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = uiState.photoUri,
                        contentDescription = stringResource(id = R.string.photo_together),
                        contentScale = ContentScale.Crop,
                        requestBuilderTransform = { request ->
                            val requestOptions = RequestOptions()
                                .placeholder(R.drawable.ic_loading_placeholder)
                                .error(R.drawable.ic_error_placeholder)
                            request.apply(requestOptions)
                        }
                    )
                    GlideImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = gameUiState.puzzle?.twibbonUrl,
                        contentDescription = stringResource(id = R.string.twibbon_template),
                        contentScale = ContentScale.Crop,
                        requestBuilderTransform = { request ->
                            val requestOptions = RequestOptions()
                                .placeholder(R.drawable.ic_loading_placeholder)
                                .error(R.drawable.ic_error_placeholder)
                            request.apply(requestOptions)
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                modifier = Modifier.align(CenterHorizontally),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Black,
                    containerColor = White
                ),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Black),
                onClick = {
                    cameraLauncher.launch(uri)
                },
            ) {
                Text(
                    text = stringResource(id = R.string.take_photo),
                    style = PoppinsMedium14
                )
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 24.dp
                )
                .align(BottomCenter)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    ,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedIconButton(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        containerColor = White,
                        contentColor = AccentYellowDark
                    ),
                    border = BorderStroke(1.dp, AccentYellowDark),
                    onClick = {
                        screenshotState.capture()
                        screenshotState.capture()
                        onSaveTwibbon(screenshotState.bitmap, GameUiState.AcknowledgeCode.TWIBBON_SHARE, Game.Status.Course, context.getString(R.string.listen_story_puzzle))
                    }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Icon(
                            imageVector = Icons.Filled.Share,
                            contentDescription = stringResource(id = R.string.download)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(id = R.string.share),
                            style = PoppinsMedium16,
                        )
                    }
                }
                OutlinedIconButton(
                    modifier = Modifier
                        .size(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = IconButtonDefaults.outlinedIconButtonColors(
                        containerColor = White,
                        contentColor = AccentYellowDark
                    ),
                    border = BorderStroke(1.dp, AccentYellowDark),
                    onClick = {
                        screenshotState.capture()
                        screenshotState.capture()
                        onSaveTwibbon(screenshotState.bitmap, GameUiState.AcknowledgeCode.TWIBBON_DOWNLOAD, Game.Status.Course, context.getString(R.string.listen_story_puzzle))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = stringResource(id = R.string.download)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FilledAccentYellowButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.next_to_story)
            ) {
                screenshotState.capture()
                screenshotState.capture()
                onSaveTwibbon(screenshotState.bitmap, GameUiState.AcknowledgeCode.TWIBBON_AND_NEXT, Game.Status.Course, context.getString(R.string.listen_story_puzzle))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun TakeTwibbonContentPreview() {
    TakeTwibbonContent(
        gameUiState = GameUiState(),
        uiState = TakeTwibbonUiState(),
        onEvent = {},
        onSaveTwibbon = {_, _, _, _ ->}
    )
}