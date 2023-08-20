package com.mahezza.mahezza.ui.features.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.mahezza.mahezza.R

data class OnBoardingUiState(
    val pageIndex : Int = 0,
    val onBoardingResources : List<OnBoardingResource> = listOf(
        OnBoardingResource(
            R.drawable.onboarding1_image,
            R.string.lorem_ipsum_dolor_si_amet,
            R.string.lorem_ipsum_dolor_si_amet_consectur
        ),
        OnBoardingResource(
            R.drawable.onboarding2_image,
            R.string.lorem_ipsum_dolor_si_amet,
            R.string.lorem_ipsum_dolor_si_amet_consectur
        ),
        OnBoardingResource(
            R.drawable.onboarding3_image,
            R.string.lorem_ipsum_dolor_si_amet,
            R.string.lorem_ipsum_dolor_si_amet_consectur
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