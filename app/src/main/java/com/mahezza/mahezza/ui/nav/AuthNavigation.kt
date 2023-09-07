package com.mahezza.mahezza.ui.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mahezza.mahezza.ui.features.children.insert.InsertChildProfileScreen
import com.mahezza.mahezza.ui.features.children.insert.InsertChildProfileViewModel
import com.mahezza.mahezza.ui.features.login.LoginScreen
import com.mahezza.mahezza.ui.features.login.LoginViewModel
import com.mahezza.mahezza.ui.features.onboarding.OnBoardingScreen
import com.mahezza.mahezza.ui.features.profile.create.CreateProfileScreen
import com.mahezza.mahezza.ui.features.profile.create.CreateProfileViewModel
import com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader.QRCodeReaderScreen
import com.mahezza.mahezza.ui.features.redeempuzzle.qrcodereader.QRCodeReaderViewModel
import com.mahezza.mahezza.ui.features.redeempuzzle.redeem.RedeemPuzzleScreen
import com.mahezza.mahezza.ui.features.redeempuzzle.redeem.RedeemPuzzleViewModel
import com.mahezza.mahezza.ui.features.register.RegisterScreen
import com.mahezza.mahezza.ui.features.register.RegisterViewModel
import com.mahezza.mahezza.ui.nav.NavArgumentConst.NEXT_ROUTE
import com.mahezza.mahezza.ui.nav.NavArgumentConst.USER_ID

@Composable
fun AuthNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.OnBoarding
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
            MainNavigation()
        }
        composableWithAnimation(
            route = "${Routes.RedeemPuzzle}?${NEXT_ROUTE}={${NEXT_ROUTE}}",
            arguments = listOf(
                navArgument(NEXT_ROUTE){
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ){entry ->
            val nextRoute = entry.arguments?.getString(NEXT_ROUTE)
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
    }
}
