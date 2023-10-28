package com.mahezza.mahezza.ui.features.children.list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
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
import com.mahezza.mahezza.ui.features.game.selectchild.components.ChildrenItem
import com.mahezza.mahezza.ui.nav.Routes
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.AccentYellowDark
import com.mahezza.mahezza.ui.theme.Black
import com.mahezza.mahezza.ui.theme.PoppinsMedium16
import com.mahezza.mahezza.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildrenListScreen(
    navController: NavController,
    drawerState: DrawerState,
    viewModel: ChildrenListViewModel,
) {
    val scope = rememberCoroutineScope()
    changeStatusBarColor(color = AccentYellow)
    val uiState = viewModel.uiState.collectAsState()
    val childrenListState = viewModel.childrenListStates.collectAsState(initial = emptyList())
    val context = LocalContext.current

    LaunchedEffect(key1 = uiState.value.generalMessage){
        uiState.value.generalMessage?.let { message ->
            showToast(context, message.asString(context))
            viewModel.onEvent(ChildrenListEvent.OnGeneralMessageShowed)
        }
    }

    ChildrenListContent(
        uiState = uiState.value,
        childrenListState = childrenListState.value,
        onEvent = viewModel::onEvent,
        onFabClick = {
            navController.navigate(route = Routes.InsertChildProfile)
        },
        onDrawerClick = {
            scope.launch {
                drawerState.open()
            }
        }
    )

}

@Composable
fun ChildrenListContent(
    uiState: ChildrenListState,
    childrenListState: List<ChildrenListState.ChildrenSummaryState>,
    onEvent: (ChildrenListEvent) -> Unit,
    onFabClick: () -> Unit,
    onDrawerClick : () -> Unit,
) {
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
                    onClick = onDrawerClick
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
                    text = stringResource(id = R.string.child_profile),
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
                    state = uiState.childrenLayoutState,
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
                            items = childrenListState,
                            key = {
                                it.name
                            },
                        ){childState ->
                            ChildrenItem(
                                modifier = Modifier.fillMaxWidth(),
                                imageUrl = childState.photoUrl,
                                name = childState.name,
                                age = childState.age,
                                lastActivity = childState.lastActivity,
                                numberOfPlay = childState.numberOfPlay,
                                numberOfCompletedChallenges = childState.numberOfCompletedChallenge,
                                timeOfPlay = childState.timeOfPlay,
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
            onClick = onFabClick
        ){
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.puzzle)
            )
        }
    }
}