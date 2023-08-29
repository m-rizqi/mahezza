package com.mahezza.mahezza.ui.features.game.selectchild

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import com.mahezza.mahezza.ui.components.FilledAccentYellowButton
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.components.ShimmerEmptyContentLayout
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.game.GameEvent
import com.mahezza.mahezza.ui.features.game.GameViewModel
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.GreyText
import com.mahezza.mahezza.ui.theme.PoppinsMedium10
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.PoppinsRegular12
import com.mahezza.mahezza.ui.theme.White

@Composable
fun SelectChildForGameScreen(
    navController: NavController,
    gameViewModel: GameViewModel,
    viewModel: SelectChildForGameViewModel
) {
    changeStatusBarColor(color = AccentYellow)
    val uiState = viewModel.uiState.collectAsState()
    val childrenCardStates = viewModel.childrenCardStates.collectAsState(initial = emptyList())
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
        }
    }

    LaunchedEffect(key1 = uiState.value.finalSelectedChildren){
        uiState.value.finalSelectedChildren?.let { children ->
            gameViewModel.onEvent(GameEvent.SetSelectedChildren(children))
            navController.navigate(Routes.SelectPuzzleForGame)
        }
    }

    SelectChildForGameContent(
        uiState = uiState.value,
        onEvent = viewModel::onEvent
    )

    LoadingScreen(isShowLoading = uiState.value.isShowLoading)
}

@Composable
fun SelectChildForGameContent(
    uiState: SelectChildForGameUiState,
    onEvent: (SelectChildForGameEvent) -> Unit,
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
                text = stringResource(id = R.string.who_play),
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
                    state = uiState.childLayoutState,
                    emptyMessage = StringResource.StringResWithParams(R.string.child_data_is_empty_or_error), 
                    shimmer = {brush ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            repeat(3){
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(88.dp)
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
                            items = uiState.selectChildCardForGameStateList,
                            key = {
                                it.id
                            },
                        ){selectChildForGameState ->
                            SelectChildCardForGame(
                                modifier = Modifier.fillMaxWidth(),
                                imageUrl = selectChildForGameState.imageUrl,
                                name = selectChildForGameState.name,
                                age = selectChildForGameState.age,
                                lastActivity = selectChildForGameState.lastActivity,
                                isChecked = selectChildForGameState.isChecked,
                                onCheckedChangeListener = selectChildForGameState.onCheckedChangeListener
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
                text = stringResource(id = R.string.next_select_puzzle)
            ) {
                onEvent(SelectChildForGameEvent.OnNextListener)
            }
        }

    }
}

@Preview
@Composable
fun SelectChildForGameContentPreview() {
    SelectChildForGameContent(
        SelectChildForGameUiState(),
    ) {

    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SelectChildCardForGame(
    modifier: Modifier = Modifier,
    imageUrl : String,
    name : String,
    age : Double,
    lastActivity : String,
    isChecked : Boolean,
    onCheckedChangeListener : (Boolean) -> Unit,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ){
            GlideImage(
                modifier = Modifier
                    .width(80.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(4.dp)),
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
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = name,
                    style = PoppinsMedium16,
                    color = Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column() {
                        Row(
                            verticalAlignment = CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(10.dp),
                                tint = GreyText,
                                painter = painterResource(id = R.drawable.ic_user),
                                contentDescription = stringResource(id = R.string.age)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = R.string.age),
                                style = PoppinsMedium10,
                                color = GreyText
                            )
                        }
                        Text(
                            text = stringResource(id = R.string.age_year, age),
                            style = PoppinsRegular12,
                            color = Black
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(10.dp),
                                tint = GreyText,
                                painter = painterResource(id = R.drawable.ic_history),
                                contentDescription = stringResource(id = R.string.last_activity)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stringResource(id = R.string.last_activity),
                                style = PoppinsMedium10,
                                color = GreyText
                            )
                        }
                        Text(
                            text = lastActivity,
                            style = PoppinsRegular12,
                            color = Black
                        )
                    }
                }
            }
            Checkbox(
                modifier = Modifier.align(CenterVertically),
                colors = CheckboxDefaults.colors(
                    checkedColor = AccentYellow,
                    checkmarkColor = Black
                ),
                checked = isChecked,
                onCheckedChange = onCheckedChangeListener
            )
        }
    }
}

@Preview
@Composable
fun SelectChildCardForGamePreview() {
    SelectChildCardForGame(
        imageUrl = "https://images.unsplash.com/photo-1499323888381-7fd102a793de?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=774&q=80",
        name = "Aliya Agnesa",
        age = 10.0,
        isChecked = true,
        lastActivity = "Mendengarkan Cerita",
        onCheckedChangeListener = {

        }
    )
}
