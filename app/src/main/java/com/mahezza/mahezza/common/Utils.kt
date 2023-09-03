package com.mahezza.mahezza.common

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        cacheDir      /* directory */
    )
    return image
}

fun Context.saveBitmapToCache(bitmap: Bitmap?, fileName: String): Uri? {
    val cacheDir = cacheDir
    val imageFile = File(cacheDir, fileName)

    try {
        FileOutputStream(imageFile).use { out ->
            bitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        return FileProvider.getUriForFile(
            this,
            applicationContext.packageName + ".provider",
            imageFile
        )
    } catch (e: IOException) {
        e.printStackTrace()
    }

    return null
}
