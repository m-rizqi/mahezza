package com.mahezza.mahezza.ui.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahezza.mahezza.ui.features.children.insert.InsertChildProfileScreen
import com.mahezza.mahezza.ui.features.children.insert.InsertChildProfileViewModel
import com.mahezza.mahezza.ui.features.dashboard.DashboardScreen
import com.mahezza.mahezza.ui.features.login.LoginScreen
import com.mahezza.mahezza.ui.features.login.LoginViewModel
import com.mahezza.mahezza.ui.features.onboarding.OnBoardingScreen
import com.mahezza.mahezza.ui.features.profile.create.CreateProfileScreen
import com.mahezza.mahezza.ui.features.profile.create.CreateProfileViewModel
import com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader.QRCodeReaderScreen
import com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader.QRCodeReaderViewModel
import com.mahezza.mahezza.ui.features.register.RegisterScreen
import com.mahezza.mahezza.ui.features.register.RegisterViewModel
import com.mahezza.mahezza.ui.nav.NavArgumentConst.USER_ID

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.QRCodeReader
    ){
        composableWithAnimation(
            route = Routes.OnBoarding
        ){
            OnBoardingScreen(navController)
        }
        composableWithAnimation(
            route = Routes.Login
        ){
            val loginViewModel : LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composableWithAnimation(
            route = Routes.Register,
        ){
            val registerViewModel : RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }
        composableWithAnimation(
            route = "${Routes.CreateProfile}?${USER_ID}={${USER_ID}}",
            arguments = listOf(
                navArgument(USER_ID){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ){entry ->
            val userId = entry.arguments?.getString(USER_ID) ?: ""
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
            route = Routes.Dashboard
        ){
            DashboardScreen(
                navController = navController,
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
    }
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
                fullWidth
            })
        },
        popEnterTransition = {
            slideInHorizontally(initialOffsetX = {fullWidth ->
                fullWidth
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