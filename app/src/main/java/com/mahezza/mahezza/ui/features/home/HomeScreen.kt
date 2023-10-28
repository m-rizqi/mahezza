package com.mahezza.mahezza.ui.features.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.ui.components.ShimmerEmptyContentLayout
import com.mahezza.mahezza.ui.components.StackedPhotoProfiles
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.home.components.ChildSummaryItem
import com.mahezza.mahezza.ui.features.home.components.PuzzleLandscapeThumbnail
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.Grey
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium10
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular10
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.PoppinsSemiBold18
import com.mahezza.mahezza.ui.theme.White
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    drawerState: DrawerState,
    homeViewModel: HomeViewModel
) {
    changeStatusBarColor(color = White)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val uiState = homeViewModel.uiState.collectAsState()
    val lastGameActivityStates = homeViewModel.lastGameActivityStates.collectAsState(initial = emptyList())
    val puzzleStates = homeViewModel.redeemedPuzzleStates.collectAsState(initial = emptyList())
    val childrenSummaryStates = homeViewModel.childrenSummaryStates.collectAsState(initial = emptyList())

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            homeViewModel.onEvent(HomeEvent.OnGeneralMessageShowed)
        }
    }

    HomeContent(
        navController = navController,
        uiState = uiState.value,
        lastGameActivityStates = lastGameActivityStates.value,
        puzzleStates = puzzleStates.value,
        childrenSummaryStates = childrenSummaryStates.value,
        onDrawerClick = {
            scope.launch {
                drawerState.open()
            }
        },
        onRedeemPuzzleClick = {
            navController.navigate(Routes.RedeemPuzzle)
        },
        onStartPlayClick = {
            navController.navigate(Routes.Game)
        }
    )
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun HomeContent(
    navController: NavController,
    uiState: HomeUiState,
    lastGameActivityStates : List<HomeUiState.LastGameActivityState>,
    puzzleStates : List<Puzzle>,
    childrenSummaryStates : List<HomeUiState.ChildrenSummaryState>,
    onDrawerClick : () -> Unit,
    onRedeemPuzzleClick : () -> Unit,
    onStartPlayClick : () -> Unit,
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                IconButton(
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = AccentYellowDark,
                        containerColor = Color.Transparent
                    ),
                    onClick = onDrawerClick
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(id = R.string.menu)
                    )
                }
                OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = White,
                        contentColor = AccentYellowDark,
                    ),
                    border = BorderStroke(1.dp, AccentYellowDark),
                    onClick = onRedeemPuzzleClick
                ) {
                    Text(
                        text = stringResource(id = R.string.redeem_puzzle),
                        style = PoppinsMedium16,
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.last_activity),
                style = PoppinsSemiBold18,
                color = AccentYellowDark
            )
            Spacer(modifier = Modifier.height(16.dp))
            ShimmerEmptyContentLayout(
                state = uiState.lastGameActivityLayoutState,
                modifier = Modifier.fillMaxWidth(),
                emptyMessage = StringResource.StringResWithParams(R.string.last_activity_not_found),
                shimmer = {brush ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(176.dp)
                                .background(brush, RoundedCornerShape(8.dp))
                        )
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(176.dp)
                                .background(brush, RoundedCornerShape(8.dp))
                        )
                    }
                }
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    items(items = lastGameActivityStates){lastGameActivityState ->
                        LastActivityCard(
                            puzzle = lastGameActivityState.puzzle,
                            lastActivity = lastGameActivityState.lastActivity,
                            children = lastGameActivityState.children,
                            elapsedTime = lastGameActivityState.elapsedTime,
                            onClick = {
                                val destination = lastGameActivityState.onClick()
                                navController.navigate(destination)
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.owned_puzzle_series),
                    style = PoppinsSemiBold18,
                    color = AccentYellowDark
                )
                Text(
                    text = stringResource(id = R.string.view_more),
                    style = PoppinsRegular12,
                    color = GreyText
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ShimmerEmptyContentLayout(
                state = uiState.puzzleLayoutState,
                modifier = Modifier.fillMaxWidth(),
                emptyMessage = StringResource.StringResWithParams(R.string.yout_dont_have_puzzle),
                shimmer = {brush ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .height(140.dp)
                                .aspectRatio(16f / 9f)
                                .background(brush, RoundedCornerShape(8.dp))
                        )
                        Spacer(
                            modifier = Modifier
                                .height(140.dp)
                                .aspectRatio(16f / 9f)
                                .background(brush, RoundedCornerShape(8.dp))
                        )
                    }
                }
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    items(items = puzzleStates){puzzleState ->
                        PuzzleLandscapeThumbnail(puzzle = puzzleState)
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.child_growth),
                    style = PoppinsSemiBold18,
                    color = AccentYellowDark
                )
                Text(
                    modifier = Modifier.
                        clickable {
                            navController.navigate(route = Routes.InsertChildProfile)
                        },
                    text = stringResource(id = R.string.view_more),
                    style = PoppinsRegular12,
                    color = GreyText
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            ShimmerEmptyContentLayout(
                state = uiState.childrenSummaryLayoutState,
                modifier = Modifier.fillMaxWidth(),
                emptyMessage = StringResource.StringResWithParams(R.string.no_children_data),
                shimmer = {brush ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(200.dp)
                                .background(brush, RoundedCornerShape(8.dp))
                        )
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .height(200.dp)
                                .background(brush, RoundedCornerShape(8.dp))
                        )
                    }
                }
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    items(items = childrenSummaryStates){childrenSummary ->
                        ChildSummaryItem(
                            childImageUrl = childrenSummary.photoUrl,
                            childName = childrenSummary.name,
                            numberOfPlay = childrenSummary.numberOfPlay,
                            timeOfPlay = childrenSummary.timeOfPlay,
                            numberOfCompletedChallenges = childrenSummary.numberOfCompletedChallenge,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(64.dp))
        }
        ExtendedFloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-16).dp, (-16).dp),
            containerColor = AccentYellow,
            contentColor = Black,
            text = {
                   Text(
                       text = stringResource(id = R.string.start_play),
                       style = PoppinsMedium16,
                   )
            },
            icon = {
                   Icon(
                       modifier = Modifier.size(24.dp),
                       painter = painterResource(id = R.drawable.ic_puzzle),
                       contentDescription = stringResource(id = R.string.puzzle)
                   )
            },
            onClick = onStartPlayClick
        )
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Preview(showSystemUi = true)
@Composable
fun HomeContentPreview() {
    HomeContent(
        navController = NavController(LocalContext.current),
        uiState = HomeUiState(),
        lastGameActivityStates = emptyList(),
        puzzleStates = emptyList(),
        childrenSummaryStates = emptyList(),
        onDrawerClick = {},
        onRedeemPuzzleClick = {},
        onStartPlayClick = {}
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun LastActivityCard(
    puzzle : Puzzle,
    lastActivity : String,
    children : List<Child>,
    elapsedTime : String,
    onClick : () -> Unit
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val cardWidth = remember {
        screenWidth / 2
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        border = BorderStroke(1.dp, Grey),
        elevation = CardDefaults.elevatedCardElevation(1.dp),
        modifier = Modifier
            .width(cardWidth.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .clip(RoundedCornerShape(8.dp))
                ,
                contentScale = ContentScale.Crop,
                model = puzzle.banner,
                contentDescription = puzzle.name,
                requestBuilderTransform = { request ->
                    val requestOptions = RequestOptions()
                        .placeholder(R.drawable.ic_loading_placeholder)
                        .error(R.drawable.ic_error_placeholder)
                    request.apply(requestOptions)
                }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = puzzle.name,
                    style = PoppinsMedium10,
                    color = AccentYellowDark
                )
                Text(
                    text = lastActivity,
                    style = PoppinsMedium14,
                    color = Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Grey)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_progress), // Temporary
                        contentDescription = stringResource(id = R.string.progress)
                    )
                    Text(
                        text = elapsedTime,
                        style = PoppinsRegular12,
                        color = Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StackedPhotoProfiles(
                        imageSize = 16.dp,
                        offset = (-10).dp,
                        photoProfiles = children.map { it.photoUrl })
                    Text(
                        text = children.joinToString { it.name },
                        style = PoppinsRegular12,
                        color = Black
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = AccentYellowDark
                        ),
                        onClick = onClick
                    ) {
                        Text(
                            text = stringResource(id = R.string.continue_game),
                            style = PoppinsRegular10,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun LastActivityCardPreview() {
    LastActivityCard(
        puzzle = Puzzle("","", "", "", "", emptyList(), ""),
        lastActivity = "",
        children = emptyList(),
        elapsedTime = "",
        onClick = {}
    )
}