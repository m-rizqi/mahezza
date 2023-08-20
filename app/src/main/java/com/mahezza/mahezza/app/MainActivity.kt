package com.mahezza.mahezza.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mahezza.mahezza.ui.features.onboarding.OnBoardingScreen
import com.mahezza.mahezza.ui.theme.MahezzaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MahezzaTheme {
                OnBoardingScreen()
            }
        }
    }
}