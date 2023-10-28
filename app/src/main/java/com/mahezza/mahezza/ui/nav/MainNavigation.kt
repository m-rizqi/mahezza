package com.mahezza.mahezza.ui.nav

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.compose.navigation
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.components.LoadingScreen
import com.mahezza.mahezza.ui.ext.getUnGrantedPermissions
import com.mahezza.mahezza.ui.ext.showToast
import com.mahezza.mahezza.ui.features.children.insert.InsertChildProfileScreen
import com.mahezza.mahezza.ui.features.children.insert.InsertChildProfileViewModel
import com.mahezza.mahezza.ui.features.children.list.ChildrenListScreen
import com.mahezza.mahezza.ui.features.children.list.ChildrenListViewModel
import com.mahezza.mahezza.ui.features.dashboard.DashboardViewModel
import com.mahezza.mahezza.ui.features.dashboard.DrawerItemsViewModel
import com.mahezza.mahezza.ui.features.dashboard.DrawerItem
import com.mahezza.mahezza.ui.features.game.GameEvent
import com.mahezza.mahezza.ui.features.game.GameViewModel
import com.mahezza.mahezza.ui.features.game.course.CourseViewModel
import com.mahezza.mahezza.ui.features.game.course.detail.GameCourseDetailScreen
import com.mahezza.mahezza.ui.features.game.course.list.GameCourseListScreen
import com.mahezza.mahezza.ui.features.game.playsession.PlaySessionScreen
import com.mahezza.mahezza.ui.features.game.playsession.PlaySessionViewModel
import com.mahezza.mahezza.ui.features.game.selectchild.SelectChildForGameScreen
import com.mahezza.mahezza.ui.features.game.selectchild.SelectChildForGameViewModel
import com.mahezza.mahezza.ui.features.game.selectpuzzle.SelectPuzzleForGameScreen
import com.mahezza.mahezza.ui.features.game.selectpuzzle.SelectPuzzleForGameViewModel
import com.mahezza.mahezza.ui.features.game.twibbon.TakeTwibbonScreen
import com.mahezza.mahezza.ui.features.game.twibbon.TakeTwibbonViewModel
import com.mahezza.mahezza.ui.features.home.HomeScreen
import com.mahezza.mahezza.ui.features.home.HomeViewModel
import com.mahezza.mahezza.ui.features.profile.create.CreateProfileScreen
import com.mahezza.mahezza.ui.features.profile.create.CreateProfileViewModel
import com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader.QRCodeReaderScreen
import com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader.QRCodeReaderViewModel
import com.mahezza.mahezza.ui.features.redeempuzzle.redeem.RedeemPuzzleScreen
import com.mahezza.mahezza.ui.features.redeempuzzle.redeem.RedeemPuzzleViewModel
import com.mahezza.mahezza.ui.nav.NavArgumentConst.GAME_ID
import com.mahezza.mahezza.ui.nav.NavArgumentConst.IS_RESUME_GAME
import com.mahezza.mahezza.ui.theme.AccentOrange
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.PoppinsMedium10
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.White
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.M)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    val drawerItemsViewModel : DrawerItemsViewModel = hiltViewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val isShowLoading = drawerItemsViewModel.isShowLoading.collectAsState()
    val isLogOut = drawerItemsViewModel.isLogOut.collectAsState()
    val generalMessage = drawerItemsViewModel.generalMessage.collectAsState()
    val context = LocalContext.current


    val unGrantedPermissions = context.getUnGrantedPermissions(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.POST_NOTIFICATIONS,
    );
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { _ -> }
    )
    LaunchedEffect(key1 = unGrantedPermissions) {
        if (unGrantedPermissions.isNotEmpty()){
            launcher.launch(unGrantedPermissions.toTypedArray())
        }
    }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    LaunchedEffect(key1 = currentRoute){
        drawerItemsViewModel.dashboardDrawerItems.find {
            it.route?.let { routePattern ->
                Regex(routePattern).find(currentRoute ?: "") != null
            } ?: false
        }?.let {
            selectedItemIndex = drawerItemsViewModel.dashboardDrawerItems.indexOf(it)
        }
    }

    LaunchedEffect(key1 = generalMessage.value){
        generalMessage.value?.let { message ->
            showToast(context, message.asString(context))
            drawerItemsViewModel.onGeneralMessageShowed()
        }
    }

    LaunchedEffect(key1 = isLogOut.value){
        if (isLogOut.value){
            navController.navigate(Routes.Auth){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
        }
    }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerShape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth(0.8f),
                drawerContainerColor = White
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .background(AccentYellow)
                            .padding(vertical = 24.dp)
                    ){
                        Image(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .height(80.dp),
                            contentScale = ContentScale.FillHeight,
                            painter = painterResource(id = R.drawable.logo_with_name),
                            contentDescription = stringResource(id = R.string.app_name)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        drawerItemsViewModel.dashboardDrawerItems.forEachIndexed { index, drawerItem ->
                            when(drawerItem){
                                is DrawerItem.Menu -> {
                                    NavigationDrawerItem(
                                        shape = RoundedCornerShape(8.dp),
                                        colors = NavigationDrawerItemDefaults.colors(
                                            selectedContainerColor = AccentYellow,
                                            unselectedContainerColor = White
                                        ),
                                        label = {
                                            Text(
                                                text = stringResource(id = drawerItem.titleResId),
                                                style = PoppinsMedium14,
                                                color = if (index == selectedItemIndex) drawerItem.selectedColor else drawerItem.unSelectedColor
                                            )
                                        },
                                        selected = index == selectedItemIndex,
                                        onClick = {
                                            selectedItemIndex = index
                                            drawerItem.route?.let { route ->
                                                navController.navigate(route)
                                            }
                                            drawerItem.onClick()
                                            scope.launch {
                                                drawerState.close()
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                modifier = Modifier
                                                    .size(16.dp),
                                                painter = painterResource(id = drawerItem.iconResId),
                                                contentDescription = stringResource(id = drawerItem.titleResId),
                                                tint = if (index == selectedItemIndex) drawerItem.selectedColor else drawerItem.unSelectedColor
                                            )
                                        },
                                        badge = {
                                            drawerItem.badgeCount?.let {
                                                Box(
                                                    modifier = Modifier
                                                        .wrapContentSize()
                                                        .clip(CircleShape)
                                                        .background(AccentOrange)
                                                ){
                                                    Text(
                                                        text = "${drawerItem.badgeCount}",
                                                        style = PoppinsMedium10,
                                                        color = White
                                                    )
                                                }
                                            }
                                        },
                                    )
                                }
                                DrawerItem.Separator -> Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = Routes.Home
        ){
            composableWithAnimation(
                route = Routes.Auth
            ){
                AuthNavigation()
            }
            composableWithAnimation(
                route = Routes.Home
            ){
                val dashboardViewModel : DashboardViewModel = it.sharedViewModel(navController = navController)
                val homeViewModel : HomeViewModel = hiltViewModel()
                HomeScreen(
                    drawerState = drawerState,
                    navController = navController,
                    homeViewModel = homeViewModel
                )
            }
            composableWithAnimation(
                route = "${Routes.RedeemPuzzle}?${NavArgumentConst.NEXT_ROUTE}={${NavArgumentConst.NEXT_ROUTE}}",
                arguments = listOf(
                    navArgument(NavArgumentConst.NEXT_ROUTE){
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ){entry ->
                val nextRoute = entry.arguments?.getString(NavArgumentConst.NEXT_ROUTE)
                val redeemPuzzleViewModel : RedeemPuzzleViewModel = hiltViewModel()
                RedeemPuzzleScreen(
                    navController = navController,
                    nextRoute = nextRoute,
                    viewModel = redeemPuzzleViewModel
                )
            }
            composableWithAnimation(
                route = Routes.QRCodeReader
            ){
                val qrCodeReaderViewModel : QRCodeReaderViewModel = hiltViewModel()
                QRCodeReaderScreen(
                    navController = navController,
                    viewModel = qrCodeReaderViewModel
                )
            }

            navigation(
                startDestination = Routes.SelectChildForGame,
                route = Routes.Game
            ){
                composableWithAnimation(
                    route = Routes.SelectChildForGame
                ){
                    val gameViewModel = it.sharedHiltViewModel<GameViewModel>(navController = navController)
                    val selectChildForGameViewModel : SelectChildForGameViewModel = hiltViewModel()
                    SelectChildForGameScreen(
                        navController = navController,
                        gameViewModel = gameViewModel,
                        viewModel = selectChildForGameViewModel
                    )
                }
                composableWithAnimation(
                    route = Routes.SelectPuzzleForGame
                ){
                    val gameViewModel = it.sharedHiltViewModel<GameViewModel>(navController = navController)
                    val selectPuzzleForGameViewModel : SelectPuzzleForGameViewModel = hiltViewModel()
                    SelectPuzzleForGameScreen(
                        navController = navController,
                        gameViewModel = gameViewModel,
                        viewModel = selectPuzzleForGameViewModel
                    )
                }
                composableWithAnimation(
                    route = "${Routes.PlaySession}?${IS_RESUME_GAME}={${IS_RESUME_GAME}}&${GAME_ID}={${GAME_ID}}",
                    arguments = listOf(
                        navArgument(IS_RESUME_GAME){
                            type = NavType.BoolType
                            defaultValue = false
                            nullable = false
                        },
                        navArgument(GAME_ID){
                            type = NavType.StringType
                            defaultValue = null
                            nullable = true
                        }
                    )
                ){
                    val isResumeGame = it.arguments?.getBoolean(IS_RESUME_GAME, false) ?: false
                    val gameId = it.arguments?.getString(GAME_ID)
                    val gameViewModel = it.sharedHiltViewModel<GameViewModel>(navController = navController)
                    val playSessionViewModel : PlaySessionViewModel = viewModel()
                    if (isResumeGame && gameId != null){
                        gameViewModel.onEvent(GameEvent.ResumeGame(gameId))
                    }
                    PlaySessionScreen(
                        navController = navController,
                        gameViewModel = gameViewModel,
                        viewModel = playSessionViewModel
                    )
                }
                composableWithAnimation(
                    route = "${Routes.TakeTwibbon}?${IS_RESUME_GAME}={${IS_RESUME_GAME}}&${GAME_ID}={${GAME_ID}}",
                    arguments = listOf(
                        navArgument(IS_RESUME_GAME){
                            type = NavType.BoolType
                            defaultValue = false
                            nullable = false
                        },
                        navArgument(GAME_ID){
                            type = NavType.StringType
                            defaultValue = null
                            nullable = true
                        }
                    )
                ){
                    val isResumeGame = it.arguments?.getBoolean(IS_RESUME_GAME, false) ?: false
                    val gameId = it.arguments?.getString(GAME_ID)
                    val gameViewModel = it.sharedHiltViewModel<GameViewModel>(navController = navController)
                    val takeTwibbonViewModel : TakeTwibbonViewModel = hiltViewModel()
                    if (isResumeGame && gameId != null){
                        gameViewModel.onEvent(GameEvent.ResumeGame(gameId))
                    }
                    TakeTwibbonScreen(
                        navController = navController,
                        gameViewModel = gameViewModel,
                        viewModel = takeTwibbonViewModel
                    )
                }
                navigation(
                    startDestination = Routes.CourseList,
                    route = "${Routes.Course}?${IS_RESUME_GAME}={${IS_RESUME_GAME}}&${GAME_ID}={${GAME_ID}}",
                    arguments = listOf(
                        navArgument(IS_RESUME_GAME){
                            type = NavType.BoolType
                            defaultValue = false
                            nullable = false
                        },
                        navArgument(GAME_ID){
                            type = NavType.StringType
                            defaultValue = null
                            nullable = true
                        }
                    )
                ){
                    composableWithAnimation(
                        route = Routes.CourseList,
                    ){
                        val isResumeGame = it.arguments?.getBoolean(IS_RESUME_GAME, false) ?: false
                        val gameId = it.arguments?.getString(GAME_ID)
                        val gameViewModel = it.sharedHiltViewModel<GameViewModel>(navController = navController, nestedParentCount = 2)
                        val courseViewModel = it.sharedHiltViewModel<CourseViewModel>(navController = navController)
                        if (isResumeGame && gameId != null){
                            gameViewModel.onEvent(GameEvent.ResumeGame(gameId))
                        }
                        GameCourseListScreen(
                            navController = navController,
                            gameViewModel = gameViewModel,
                            courseViewModel = courseViewModel,
                        )
                    }
                    composableWithAnimation(
                        route = "${Routes.CourseDetail}/{${NavArgumentConst.SUB_COURSE_ID}}",
                        arguments = listOf(
                            navArgument(NavArgumentConst.SUB_COURSE_ID){
                                type = NavType.StringType
                            }
                        )
                    ){
                        val subCourseId = it.arguments?.getString(NavArgumentConst.SUB_COURSE_ID) ?: ""
                        val gameViewModel = it.sharedHiltViewModel<GameViewModel>(navController = navController, nestedParentCount = 2)
                        val courseViewModel = it.sharedHiltViewModel<CourseViewModel>(navController = navController)
                        GameCourseDetailScreen(
                            navController = navController,
                            gameViewModel = gameViewModel,
                            courseViewModel = courseViewModel,
                            subCourseId = subCourseId
                        )
                    }
                }
            }

            composableWithAnimation(
                route = "${Routes.CreateProfile}?${NavArgumentConst.USER_ID}={${NavArgumentConst.USER_ID}}",
                arguments = listOf(
                    navArgument(NavArgumentConst.USER_ID){
                        type = NavType.StringType
                        defaultValue = ""
                    }
                )
            ){entry ->
                val userId = entry.arguments?.getString(NavArgumentConst.USER_ID) ?: ""
                val createProfileViewModel : CreateProfileViewModel = hiltViewModel()
                CreateProfileScreen(
                    navController = navController,
                    userId = userId,
                    viewModel = createProfileViewModel
                )
            }

            composableWithAnimation(
                route = Routes.InsertChildProfile
            ){
                val insertChildProfileViewModel : InsertChildProfileViewModel = hiltViewModel()
                InsertChildProfileScreen(
                    navController = navController,
                    viewModel = insertChildProfileViewModel
                )
            }
            composableWithAnimation(
                route = Routes.ChildrenList
            ){
                val childrenListViewModel : ChildrenListViewModel = hiltViewModel()
                ChildrenListScreen(
                    navController = navController,
                    drawerState = drawerState,
                    viewModel = childrenListViewModel,
                )
            }
        }
    }
    
    LoadingScreen(isShowLoading = isShowLoading.value)
}

@Preview
@Composable
fun DashboardNavigationPreview() {
    DashboardNavigationPreview()
}