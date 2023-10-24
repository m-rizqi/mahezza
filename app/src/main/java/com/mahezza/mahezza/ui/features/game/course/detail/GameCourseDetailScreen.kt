package com.mahezza.mahezza.ui.features.game.course.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.StackedPhotoProfiles
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.features.game.GameViewModel
import com.mahezza.mahezza.ui.features.game.course.CourseEvent
import com.mahezza.mahezza.ui.features.game.course.CourseUiState
import com.mahezza.mahezza.ui.features.game.course.CourseViewModel
import com.mahezza.mahezza.ui.features.game.selectpuzzle.SelectPuzzleForGameEvent
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsRegular14
import com.mahezza.mahezza.ui.theme.PoppinsRegular16
import com.mahezza.mahezza.ui.theme.White

@Composable
fun GameCourseDetailScreen(
    navController: NavController,
    gameViewModel : GameViewModel,
    courseViewModel : CourseViewModel,
    subCourseId : String
) {
    changeStatusBarColor(color = AccentYellow)
    val context = LocalContext.current
    courseViewModel.onEvent(CourseEvent.OpenSubCourse(subCourseId))
    val gameUiState = gameViewModel.uiState.collectAsState()
    val courseUiState = courseViewModel.uiState.collectAsState()

    GameCourseDetailContent(
        children = gameUiState.value.children,
        puzzle = gameUiState.value.puzzle,
        subCourseState = courseUiState.value.openedSubCourseState,
        onSubCourseCompleted = {
            courseUiState.value.openedSubCourseState?.id?.let { subCourseId ->
                courseViewModel.onEvent(CourseEvent.OnSubCourseCompleted(subCourseId))
            }
            navController.popBackStack()
        },
        onBack = {navController.popBackStack()}
    )
    
    LoadingScreen(isShowLoading = gameUiState.value.isLoading || courseUiState.value.isShowLoading)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GameCourseDetailContent(
    children : List<Child>,
    puzzle: Puzzle?,
    subCourseState: CourseUiState.SubCourseState?,
    onSubCourseCompleted : () -> Unit,
    onBack : () -> Unit
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(White)
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AccentYellow)
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = Black
                    ),
                    onClick = onBack
                ) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
                Spacer(modifier = Modifier.width(0.dp))
                StackedPhotoProfiles(
                    photoProfiles = children.map { it.photoUrl },
                    imageSize = 32.dp,
                    offset = (-20).dp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    modifier = Modifier.weight(1f),
                    text = "${subCourseState?.name} • ${puzzle?.name}",
                    style = PoppinsMedium16,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(104.dp)
                            .background(AccentYellow)
                            .padding(16.dp)
                    ) {
                        subCourseState?.illustrationUrl?.let { illustration ->
                            GlideImage(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .align(BottomEnd)
                                ,
                                contentScale = ContentScale.Crop,
                                model = illustration,
                                contentDescription = null,
                                requestBuilderTransform = { request ->
                                    val requestOptions = RequestOptions()
                                        .placeholder(R.drawable.ic_loading_placeholder)
                                        .error(R.drawable.ic_error_placeholder)
                                    request.apply(requestOptions)
                                }
                            )
                        }

                    }
                }
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(32.dp)
                    ) {
                        subCourseState?.contentStates?.forEach { contentState ->
                            ContentCourse(contentState = contentState)
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .align(Alignment.BottomCenter)
        ) {
            FilledAccentYellowButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.course_taught)
            ) {
                onSubCourseCompleted()
            }
        }
    }
}

@Preview
@Composable
fun GameCourseDetailContentPreview() {
    GameCourseDetailContent(
        children = emptyList(),
        puzzle = null,
        subCourseState = null,
        onSubCourseCompleted = {},
        onBack = {}
    )
}

@Composable
fun ContentCourse(
    contentState: CourseUiState.ContentState
) {
    when(contentState){
        is CourseUiState.ContentState.ChallengeState -> ChallengeContentCourse(
            modifier = Modifier.fillMaxWidth(),
            challengeState = contentState
        )
        is CourseUiState.ContentState.ImageState -> ImageContentCourse(
            modifier = Modifier.fillMaxWidth(),
            imageState = contentState
        )
        is CourseUiState.ContentState.ScriptState -> ScriptContentCourse(
            modifier = Modifier.fillMaxWidth(),
            scriptState = contentState
        )
        is CourseUiState.ContentState.VideoState -> VideoContentCourse(
            modifier = Modifier.fillMaxWidth(),
            videoState = contentState
        )
    }
}

@Composable
fun ChallengeContentCourse(
    modifier: Modifier = Modifier,
    challengeState: CourseUiState.ContentState.ChallengeState
) {
    Column(
        modifier = modifier
            .background(White)
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = if (challengeState.isCompleted) AccentYellow else GreyText,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Row(modifier = Modifier
            .background(
                if (challengeState.isCompleted) AccentYellow else GreyText
            )
            .fillMaxWidth()
            .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.challenge_of_challenges, challengeState.challengeNumber, challengeState.numberOfChallenges),
                style = PoppinsRegular12,
                color = if (challengeState.isCompleted) Black else White
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = challengeState.title,
                style = PoppinsMedium16,
                color = Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = challengeState.content,
                style = PoppinsRegular14,
                color = Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .align(CenterHorizontally)
                    .clickable {
                        challengeState.onCompletionClick()
                    },
                text = stringResource(
                    id = if (challengeState.isCompleted) R.string.challenge_completed else R.string.mark_completed
                ),
                style = PoppinsRegular12,
                color = if (challengeState.isCompleted) GreyText else AccentYellowDark
            )
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageContentCourse(
    modifier: Modifier = Modifier,
    imageState: CourseUiState.ContentState.ImageState
) {
    GlideImage(
        modifier = modifier
            .aspectRatio(1.7f)
            .clip(RoundedCornerShape(8.dp))
        ,
        contentScale = ContentScale.Crop,
        model = imageState.content,
        contentDescription = null,
        requestBuilderTransform = { request ->
            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_loading_placeholder)
                .error(R.drawable.ic_error_placeholder)
            request.apply(requestOptions)
        }
    )
}

@Composable
fun ScriptContentCourse(
    modifier: Modifier = Modifier,
    scriptState: CourseUiState.ContentState.ScriptState
) {
    Text(
        modifier = modifier,
        text = scriptState.content,
        style = PoppinsRegular16,
        color = Black
    )
}

@Composable
fun VideoContentCourse(
    modifier: Modifier = Modifier,
    videoState: CourseUiState.ContentState.VideoState
) {
    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    val applicationContext = LocalContext.current.applicationContext
    val player = remember {
        ExoPlayer.Builder(applicationContext).build().also {
            it.setMediaItem(MediaItem.fromUri(videoState.content))
        }
    }
    AndroidView(
        modifier = modifier
            .aspectRatio(16 / 9f)
            .clickable {
                player.play()
            },
        factory = {context ->
            PlayerView(context).also {
                it.player = player
            }
        },
        update = {
            when (lifecycle) {
                Lifecycle.Event.ON_PAUSE -> {
                    it.onPause()
                    it.player?.pause()
                }
                Lifecycle.Event.ON_RESUME -> {
                    it.onResume()
                }
                else -> Unit
            }
        },
    )
}

@Preview
@Composable
fun ContentCoursePreview() {
    ContentCourse(contentState = CourseUiState.ContentState.ImageState("", "", ""))
}

