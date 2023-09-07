package com.mahezza.mahezza.ui.features.game.course.list

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.common.saveBitmapToCache
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.data.model.SubCourse
import com.mahezza.mahezza.ui.components.LayoutState
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.ShimmerEmptyContentLayout
import com.mahezza.mahezza.ui.components.StackedPhotoProfiles
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.game.ExitGameDialog
import com.mahezza.mahezza.ui.features.game.GameEvent
import com.mahezza.mahezza.ui.features.game.GameFinishedDialog
import com.mahezza.mahezza.ui.features.game.GameUiState
import com.mahezza.mahezza.ui.features.game.GameViewModel
import com.mahezza.mahezza.ui.features.game.course.CourseEvent
import com.mahezza.mahezza.ui.features.game.course.CourseUiState
import com.mahezza.mahezza.ui.features.game.course.CourseViewModel
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentBlue
import com.mahezza.mahezza.ui.theme.AccentGreen
import com.mahezza.mahezza.ui.theme.AccentOrange
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.Grey
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular10
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.RedDanger
import com.mahezza.mahezza.ui.theme.White

@Composable
fun GameCourseListScreen(
    navController: NavController,
    gameViewModel : GameViewModel,
    courseViewModel: CourseViewModel
) {
    changeStatusBarColor(color = White)
    val context = LocalContext.current
    val gameUiState = gameViewModel.uiState.collectAsState()
    val courseUiState = courseViewModel.uiState.collectAsState()
    var isShowFinishDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = gameUiState.value){
        gameUiState.value.puzzle?.let {
            courseViewModel.onEvent(CourseEvent.SetPuzzleId(it.id))
        }
        gameUiState.value.acknowledgeCode?.let {acknowledgeCode ->
            if (acknowledgeCode == GameUiState.AcknowledgeCode.COURSE_FINISHED){
                isShowFinishDialog = true
            }
            if (acknowledgeCode == GameUiState.AcknowledgeCode.COURSE_EXIT){
                navController.navigate(route = Routes.Home){
                    popUpTo(Routes.Home){
                        inclusive = true
                    }
                }
            }
            gameViewModel.onEvent(GameEvent.OnSaveGameStatusAcknowledged)
        }
    }

    LaunchedEffect(key1 = courseUiState.value){
        with(courseUiState.value){
            generalMessage?.let { message ->
                showToast(context, message.asString(context))
                courseViewModel.onEvent(CourseEvent.OnGeneralMessageShowed)
            }
            openSubCourseDetail?.let {
                navController.navigate("${Routes.CourseDetail}/${it.subCourseId}")
                courseViewModel.onEvent(CourseEvent.OnSubCourseDetailOpened)
            }
        }
    }

    if(courseUiState.value.isCourseCompleted){
        gameViewModel.onEvent(GameEvent.SaveGame(
            course = courseUiState.value.courseState,
            acknowledgeCode = GameUiState.AcknowledgeCode.COURSE_FINISHED,
            lastActivity = stringResource(id = R.string.puzzle_finished),
            isGameFinished = true
        ))
    }

    if (isShowFinishDialog){
        GameFinishedDialog(
            onDismissRequest = {
                isShowFinishDialog = false
                navController.navigate(route = Routes.Home){
                    popUpTo(Routes.Home){
                        inclusive = true
                    }
                }
            },
            elapsedTimeInMinutes = gameUiState.value.getElapsedTimeInMinute(),
            subCourseCount = courseUiState.value.courseState?.subCourseStates?.size ?: 0,
            challengeCompleted = courseUiState.value.courseState?.getCompletedChallenge() ?: 0,
            twibbonUrl = gameUiState.value.twibbonUrl.toString(),
            children = gameUiState.value.children,
            bannerUrl = gameUiState.value.puzzle?.banner.toString()
        ) {
            val imageFileName = "${gameUiState.value.puzzle?.name}-${gameUiState.value.children.joinToString { it.name }}.jpg"
            val imageUri = context.saveBitmapToCache(gameUiState.value.twibbon, imageFileName)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
            shareIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.share_text))
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_twibbon_mahezza)))
        }
    }

    GameCourseListContent(
        children = gameUiState.value.children,
        puzzle = gameUiState.value.puzzle,
        courseUiState = courseUiState.value,
        onEventCourse = courseViewModel::onEvent,
        onSaveCourseWithAckCode = {acknowledgeCode ->
            gameViewModel.onEvent(GameEvent.SaveGame(course = courseUiState.value.courseState, acknowledgeCode = acknowledgeCode))
        }
    )

    LoadingScreen(isShowLoading = courseUiState.value.isShowLoading || gameUiState.value.isLoading)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GameCourseListContent(
    children : List<Child>,
    puzzle : Puzzle?,
    courseUiState: CourseUiState,
    onEventCourse : (CourseEvent) -> Unit,
    onSaveCourseWithAckCode : (GameUiState.AcknowledgeCode) -> Unit
) {
    val context = LocalContext.current
    var isShowExitDialog by remember {
        mutableStateOf(false)
    }
    if (isShowExitDialog){
        ExitGameDialog(
            onExit = {
                isShowExitDialog = false
                onSaveCourseWithAckCode(GameUiState.AcknowledgeCode.COURSE_EXIT)
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
    ){
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()){
                    Row(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        StackedPhotoProfiles(
                            photoProfiles = children.map { it.photoUrl },
                            imageSize = 48.dp,
                            offset = (-28).dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = children.joinToString { it.name },
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
                                    painter = painterResource(id = R.drawable.ic_story),
                                    contentDescription = stringResource(id = R.string.story)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = stringResource(id = R.string.story),
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
                ShimmerEmptyContentLayout(
                    modifier = Modifier.fillMaxWidth(),
                    state = courseUiState.layoutState,
                    emptyMessage = StringResource.StringResWithParams(R.string.course_is_empty_or_not_found),
                    shimmer = {brush ->
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Spacer(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(100.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(brush)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1.7f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(brush)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            repeat(3){
                                Spacer(
                                    modifier = Modifier
                                        .height(80.dp)
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(brush)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                text = courseUiState.courseState?.name ?: "",
                                style = PoppinsMedium16,
                                color = Black
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        GlideImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1.7f)
                                .clip(RoundedCornerShape(8.dp))
                            ,
                            contentScale = ContentScale.Crop,
                            model = courseUiState.courseState?.banner,
                            contentDescription = courseUiState.courseState?.name ?: "",
                            requestBuilderTransform = { request ->
                                val requestOptions = RequestOptions()
                                    .placeholder(R.drawable.ic_loading_placeholder)
                                    .error(R.drawable.ic_error_placeholder)
                                request.apply(requestOptions)
                            }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GameSubCourseCardList(
                            subCourseStates = courseUiState.courseState?.subCourseStates
                        )
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun GameCourseListContentPreview() {
    GameCourseListContent(
        children = emptyList(),
        puzzle = Puzzle("", "", "", "", "", emptyList(), ""),
        courseUiState = CourseUiState(),
        onEventCourse = {},
        onSaveCourseWithAckCode = {}
    )
}

@Composable
fun GameSubCourseCardList(
    subCourseStates : List<CourseUiState.SubCourseState>?
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        subCourseStates?.forEachIndexed{index, subCourse ->
            GameSubCourseCard(
                modifier = Modifier.fillMaxWidth(),
                indexOfCard = index,
                subCourseState = subCourse
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameSubCourseCard(
    modifier : Modifier = Modifier,
    indexOfCard : Int,
    subCourseState: CourseUiState.SubCourseState
) {
    val progressColor = listOf(
        AccentYellow,
        AccentOrange,
        AccentGreen,
        AccentBlue
    )
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        onClick = subCourseState.onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(
                    id = if (subCourseState.isCompleted) R.drawable.ic_checkmark_checked else R.drawable.ic_checkmark_unchecked
                ),
                contentDescription = stringResource(
                    id = if (subCourseState.isCompleted) R.string.finished else R.string.unfinished
                )
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = subCourseState.name,
                    style = PoppinsMedium16,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.num_of_num_challenges_completed, subCourseState.numberOfCompletedChallenges, subCourseState.numberOfChallenges),
                    style = PoppinsRegular10,
                    color = Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp)),
                    color = progressColor[indexOfCard],
                    progress = subCourseState.progress,
                    trackColor = Grey
                )
            }
        }
    }
}