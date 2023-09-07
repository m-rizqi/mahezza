package com.mahezza.mahezza.ui.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Composable
fun MahezzaNavigation(
    isLoggedIn : Boolean
) {
    if (isLoggedIn){
        MainNavigation()
    } else {
        AuthNavigation()
    }
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController, nestedParentCount : Int = 1): T {
    var nestedParent = destination.parent
    var count = 0

    while (count < nestedParentCount - 1) {
        nestedParent = nestedParent?.parent
        count++
    }

    val navGraphRoute = nestedParent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedHiltViewModel(navController: NavController, nestedParentCount: Int = 1): T {
    var nestedParent = destination.parent
    var count = 0

    while (count < nestedParentCount - 1) {
        nestedParent = nestedParent?.parent
        count++
    }

    val navGraphRoute = nestedParent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

fun NavGraphBuilder.composableWithAnimation(
    route : String,
    arguments : List<NamedNavArgument> = emptyList(),
    content : @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
){
    composable(
        route = route,
        arguments = arguments,
        enterTransition = {
            slideInHorizontally(initialOffsetX = {fullWidth ->
                fullWidth
            })
        },
        exitTransition = {
            slideOutHorizontally(targetOffsetX = {fullWidth ->
                -fullWidth
            })
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = {fullWidth ->
                -fullWidth
            })
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = {fullWidth ->
                fullWidth
            })
        },
        content = content
    )
}