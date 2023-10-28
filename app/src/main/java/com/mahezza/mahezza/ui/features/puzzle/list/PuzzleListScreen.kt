package com.mahezza.mahezza.ui.features.puzzle.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mahezza.mahezza.R
import com.mahezza.mahezza.common.StringResource
import com.mahezza.mahezza.ui.components.ShimmerEmptyContentLayout
import com.mahezza.mahezza.ui.ext.changeStatusBarColor
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.children.list.ChildrenListEvent
import com.mahezza.mahezza.ui.features.game.selectchild.components.ChildrenItem
import com.mahezza.mahezza.ui.features.puzzle.list.components.PuzzleItem
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PuzzleListScreen(
    navController: NavController,
    drawerState: DrawerState,
    viewModel: PuzzleListViewModel,
) {
    val scope = rememberCoroutineScope()
    changeStatusBarColor(color = AccentYellow)
    val uiState = viewModel.uiState.collectAsState()
    val puzzleState = viewModel.redeemedPuzzleStates.collectAsState(initial = emptyList())
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(PuzzleListEvent.OnGeneralMessageShowed)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AccentYellow)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(AccentYellow),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
            ){
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = AccentYellowDark,
                        containerColor = Color.Transparent
                    ),
                    onClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                ){
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_menu),
                        tint = Black,
                        contentDescription = stringResource(id = R.string.menu)
                    )
                }
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = stringResource(id = R.string.puzzle),
                    style = PoppinsMedium16,
                    color = Black,
                )
            }
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
                    state = uiState.value.puzzleLayoutState,
                    emptyMessage = StringResource.StringResWithParams(R.string.puzzle_data_is_empty_or_error),
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
                            items = puzzleState.value,
                            key = {
                                it.id
                            },
                        ){puzzle ->
                            PuzzleItem(
                                imageUrl = puzzle.banner,
                                name = puzzle.name,
                                description = puzzle.description,
                                onClickListener = {

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
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset((-16).dp, (-16).dp),
            containerColor = AccentYellow,
            contentColor = Black,
            onClick = {
                navController.navigate(Routes.RedeemPuzzle)
            }
        ){
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.puzzle)
            )
        }
    }
}