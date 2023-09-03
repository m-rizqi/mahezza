package com.mahezza.mahezza.domain.common

import android.content.Context
import android.graphics.Bitmap
import android.view.View

interface DownloadTwibbonUseCase {
    suspend operator fun invoke(
        context: Context,
        bitmap: Bitmap,
        puzzleName: String,
        childNames: List<String>
    )
}