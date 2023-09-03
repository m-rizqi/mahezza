package com.mahezza.mahezza.ui.features.game.twibbon

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle
import com.mahezza.mahezza.domain.common.DownloadTwibbonUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TakeTwibbonViewModel @Inject constructor(
    private val downloadTwibbonUseCase: DownloadTwibbonUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TakeTwibbonUiState())
    val uiState : StateFlow<TakeTwibbonUiState>
        get() = _uiState.asStateFlow()

    fun onEvent(event: TakeTwibbonEvent){
        when(event){
            TakeTwibbonEvent.OnGeneralMessageShowed -> {
                _uiState.update { it.copy(generalMessage = null) }
            }
            is TakeTwibbonEvent.SetPhotoUri -> {
                _uiState.update { it.copy(photoUri = event.uri) }
            }
            is TakeTwibbonEvent.DownloadTwibbon -> {
                downloadTwibbon(
                    event.context,
                    event.bitmap,
                    event.puzzle,
                    event.children
                )
            }
        }
    }

    private fun downloadTwibbon(context : Context, bitmap: Bitmap, puzzle: Puzzle?, children : List<Child>) {
        viewModelScope.launch {
            downloadTwibbonUseCase(context, bitmap, puzzle?.name ?: "", children.map { it.name })
        }
    }
}