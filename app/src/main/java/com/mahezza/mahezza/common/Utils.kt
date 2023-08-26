package com.mahezza.mahezza.common

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException

suspend fun loadBitmapFromUri(contentResolver: ContentResolver, imageUri: Uri, @IODispatcher dispatcher: CoroutineDispatcher): Bitmap? =
    withContext(dispatcher) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            return@withContext inputStream?.use { stream ->
                BitmapFactory.decodeStream(stream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return@withContext null
        }
    }