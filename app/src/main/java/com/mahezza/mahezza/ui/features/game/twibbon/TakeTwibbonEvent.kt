package com.mahezza.mahezza.ui.features.game.twibbon

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import com.mahezza.mahezza.data.model.Child
import com.mahezza.mahezza.data.model.Puzzle

sealed class TakeTwibbonEvent {
    object OnGeneralMessageShowed : TakeTwibbonEvent()
    class SetPhotoUri(val uri : Uri) : TakeTwibbonEvent()
    class DownloadTwibbon(
            val context: Context,
            val bitmap: Bitmap,
            val puzzle: Puzzle?,
            val children : List<Child>
        ) : TakeTwibbonEvent()
}