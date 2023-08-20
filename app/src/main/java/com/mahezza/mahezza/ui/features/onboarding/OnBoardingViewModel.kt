package com.mahezza.mahezza.ui.features.onboarding

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class OnBoardingViewModel() : ViewModel() {

    private val _uiState = MutableStateFlow<OnBoardingUiState>(OnBoardingUiState())
    val uiState = _uiState.asStateFlow()

    fun isLastPage(currentPage: Int, pageCount : Int) : Boolean = currentPage == (pageCount-1)
}