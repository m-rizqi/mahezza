package com.mahezza.mahezza.ui.nav

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mahezza.mahezza.ui.features.login.LoginScreen
import com.mahezza.mahezza.ui.features.login.LoginViewModel
import com.mahezza.mahezza.ui.features.onboarding.OnBoardingScreen
import com.mahezza.mahezza.ui.features.profile.create.CreateProfileScreen
import com.mahezza.mahezza.ui.features.register.RegisterScreen
import com.mahezza.mahezza.ui.features.register.RegisterViewModel

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Register){
        composable(Routes.OnBoarding){
            OnBoardingScreen(navController)
        }
        composable(
            route = Routes.Login,
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
        ){
            val loginViewModel : LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
        composable(
            route = Routes.Register,
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
        ){
            val registerViewModel : RegisterViewModel = hiltViewModel()
            RegisterScreen(navController, registerViewModel)
        }
        composable(
            route = Routes.CreateProfile,
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
        ){
            CreateProfileScreen(navController)
        }
    }
}