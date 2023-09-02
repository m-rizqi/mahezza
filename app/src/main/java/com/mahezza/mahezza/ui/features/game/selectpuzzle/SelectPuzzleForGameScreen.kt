package com.mahezza.mahezza.ui.features.game.selectpuzzle

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.LayoutState
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.ShimmerEmptyContentLayout
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.game.GameEvent
import com.mahezza.mahezza.ui.features.game.GameUiState
import com.mahezza.mahezza.ui.features.game.GameViewModel
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyBorder
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.White

@Composable
fun SelectPuzzleForGameScreen(
    navController: NavController,
    gameViewModel: GameViewModel,
    viewModel: SelectPuzzleForGameViewModel
) {
    changeStatusBarColor(color = AccentYellow)
    val uiState = viewModel.uiState.collectAsState()
    val gameUiState = gameViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(SelectPuzzleForGameEvent.OnGeneralMessageShowed)
        }
    }

    LaunchedEffect(key1 = gameUiState.value.generalMessage){
        gameUiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            gameViewModel.onEvent(GameEvent.OnGeneralMessageShowed)
        }
    }

    LaunchedEffect(key1 = uiState.value.selectedPuzzle){
        uiState.value.selectedPuzzle?.let { puzzle ->
            gameViewModel.onEvent(GameEvent.SetSelectedPuzzle(puzzle, GameUiState.GameStepSaved.STARTED))
        }
    }

    LaunchedEffect(key1 = gameUiState.value.gameStepSaved){
        if (gameUiState.value.gameStepSaved?.name == GameUiState.GameStepSaved.STARTED.name){
            navController.navigate(Routes.PlaySession)
            gameViewModel.onEvent(GameEvent.OnSaveGameStatusAcknowledged(GameUiState.GameStepSaved.STARTED))
            viewModel.onEvent(SelectPuzzleForGameEvent.OnNavigatedToPlaySession)
        }
    }

    SelectPuzzleForGameContent(
        uiState = uiState.value,
        onEvent = viewModel::onEvent
    )

    LoadingScreen(isShowLoading = uiState.value.isShowLoading || gameUiState.value.isLoading)
}

@Composable
fun SelectPuzzleForGameContent(
    uiState: SelectPuzzleForGameUiState,
    onEvent: (SelectPuzzleForGameEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AccentYellow)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AccentYellow),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.what_play),
                style = PoppinsMedium16,
                color = Black
            )
            Spacer(modifier = Modifier.height(56.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(White)
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                ShimmerEmptyContentLayout(
                    modifier = Modifier.fillMaxSize(),
                    state = uiState.puzzleLayoutState,
                    emptyMessage = StringResource.StringResWithParams(R.string.puzzle_data_is_empty_or_error),
                    shimmer = {brush ->
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            repeat(2){
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(brush)
                                )
                            }
                        }
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        items(
                            items = uiState.puzzleCardStateList,
                            key = {
                                it.id
                            },
                        ){puzzleCardState ->
                            SelectPuzzleCardForGame(
                                imageUrl = puzzleCardState.banner,
                                name = puzzleCardState.name,
                                description = puzzleCardState.description,
                                isChecked = puzzleCardState.isChecked,
                                onClickListener = {
                                    puzzleCardState.onClickListener(puzzleCardState)
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(48.dp))
                        }
                    }
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
                text = stringResource(id = R.string.play_puzzle)
            ) {
                onEvent(SelectPuzzleForGameEvent.OnNextListener)
            }
        }

    }
}

@Preview
@Composable
fun SelectPuzzleForGameContentPreview() {
    SelectPuzzleForGameContent(
        SelectPuzzleForGameUiState()
    ){

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SelectPuzzleCardForGame(
    modifier: Modifier = Modifier,
    imageUrl : String,
    name : String,
    description : String,
    isChecked : Boolean,
    onClickListener : () -> Unit,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.7f)
                    .clip(RoundedCornerShape(8.dp)),
                model = imageUrl,
                contentDescription = name,
                contentScale = ContentScale.Crop,
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
                    .padding(16.dp)
            ) {
                Text(
                    text = name,
                    style = PoppinsMedium16,
                    color = Black
                )
                Text(
                    text = description,
                    style = PoppinsRegular12,
                    color = GreyText,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isChecked) AccentYellow else White,
                            contentColor = if (isChecked) Black else GreyText,
                        ),
                        border = BorderStroke(1.dp, if (isChecked) AccentYellow else GreyBorder),
                        shape = RoundedCornerShape(4.dp),
                        onClick = onClickListener
                    ) {
                        Text(
                            text = stringResource(id = if (isChecked) R.string.puzzle_played else R.string.select_puzzle),
                            style = PoppinsRegular12,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SelectPuzzleCardForGamePreview() {
    SelectPuzzleCardForGame(
        imageUrl = "https://firebasestorage.googleapis.com/v0/b/mahezza-c3ed3.appspot.com/o/puzzles%2Fa80d4898-4278-11ee-be56-0242ac120002%2FGroup%20100.png?alt=media&token=4e26ff2b-8edd-42f1-8bba-9426b2ccce71",
        name = "Aliya Agnesa",
        description = "Deskripsi singkat tentang puzzle, lorem ipsum dolor siamet",
        isChecked = true,
        onClickListener = {

        }
    )
}