package com.mahezza.mahezza.ui.features.onboarding

import android.graphics.drawable.Drawable
import com.mahezza.mahezza.util.StringResource

data class OnBoardingUiState(
    val image : Drawable,
    val title : StringResource,
    val subTitle : StringResource
)