package com.mahezza.mahezza.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.ext.disableScreen
import com.mahezza.mahezza.ui.ext.enableScreen
import com.mahezza.mahezza.ui.theme.Grey

@Composable
fun LoadingScreen(
    isShowLoading : Boolean
) {
    if (isShowLoading){
        disableScreen()
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color(0x4D131313))
        ){
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.puzzle_loading))
            val progress by animateLottieCompositionAsState(composition = composition, iterations = Integer.MAX_VALUE)
            LottieAnimation(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(100.dp),
                composition = composition,
                progress = progress,
            )
        }
    }else{
        enableScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun LoadingScreenPreview() {
    LoadingScreen(true)
}