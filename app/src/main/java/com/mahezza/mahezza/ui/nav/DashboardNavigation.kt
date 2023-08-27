package com.mahezza.mahezza.ui.nav

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.features.dashboard.DashboardViewModel
import com.mahezza.mahezza.ui.features.dashboard.DrawerItemsViewModel
import com.mahezza.mahezza.ui.features.dashboard.DrawerItem
import com.mahezza.mahezza.ui.features.home.HomeScreen
import com.mahezza.mahezza.ui.theme.AccentOrange
import com.mahezza.mahezza.ui.theme.AccentYellow
import com.mahezza.mahezza.ui.theme.PoppinsMedium10
import com.mahezza.mahezza.ui.theme.PoppinsMedium14
import com.mahezza.mahezza.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardNavigation() {
    val navController = rememberNavController()
    val drawerItemsViewModel : DrawerItemsViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
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
                route = Routes.Home
            ){
                val dashboardViewModel : DashboardViewModel = it.sharedViewModel(navController = navController)
                HomeScreen(
                    navController = navController
                )
            }
        }
    }
}

@Preview
@Composable
fun DashboardNavigationPreview() {
    DashboardNavigationPreview()
}