package com.mahezza.mahezza.data.source.firebase.storage

import android.graphics.Bitmap

data class ImageRequest(
    val id: String,
    val bitmap: Bitmap?,
    val mimeType: String = "image/jpeg",
    val compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    val fileExtension: String = "jpg",
)