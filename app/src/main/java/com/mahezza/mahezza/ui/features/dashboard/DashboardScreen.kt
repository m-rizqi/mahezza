package com.mahezza.mahezza.ui.features.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.mahezza.mahezza.ui.theme.White

@Composable
fun DashboardScreen(
    navController: NavController
) {
    Box(modifier = Modifier.fillMaxSize().background(White))
}