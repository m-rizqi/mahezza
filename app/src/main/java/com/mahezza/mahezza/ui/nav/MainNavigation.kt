package com.mahezza.mahezza.ui.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mahezza.mahezza.ui.features.login.LoginScreen
import com.mahezza.mahezza.ui.features.login.LoginViewModel
import com.mahezza.mahezza.ui.features.onboarding.OnBoardingScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Login){
        composable(Routes.OnBoarding){
            OnBoardingScreen(navController)
        }
        composable(Routes.Login){
            val loginViewModel : LoginViewModel = hiltViewModel()
            LoginScreen(navController, loginViewModel)
        }
    }
}