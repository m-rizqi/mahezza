package com.mahezza.mahezza.ui.features.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mahezza.mahezza.R

data class OnBoardingUiState(
    val pageIndex : Int = 0,
    val onBoardingResources : List<OnBoardingResource> = listOf(
        OnBoardingResource(
            R.drawable.onboarding1_image,
            R.string.on_boarding_title_1,
            R.string.on_boarding_desc_1
        ),
        OnBoardingResource(
            R.drawable.onboarding2_image,
            R.string.on_boarding_title_2,
            R.string.on_boarding_desc_2
        ),
        OnBoardingResource(
            R.drawable.onboarding3_image,
            R.string.on_boarding_title_3,
            R.string.on_boarding_desc_3
        )
    )
) {
    data class OnBoardingResource(
        @DrawableRes
        val imageResId : Int,
        @StringRes
        val titleResId : Int,
        @StringRes
        val subTitleResId : Int
    )

}