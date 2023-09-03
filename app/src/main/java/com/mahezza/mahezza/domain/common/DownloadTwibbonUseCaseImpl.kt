package com.mahezza.mahezza.domain.common

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.content.FileProvider
import com.mahezza.mahezza.di.DownloadImageNotificationBuilder
import com.mahezza.mahezza.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import javax.inject.Inject

class DownloadTwibbonUseCaseImpl @Inject constructor(
    private val notificationManager: NotificationManager,
    @DownloadImageNotificationBuilder
    private val notificationBuilder : NotificationCompat.Builder,
    @IODispatcher
    private val dispatcher: CoroutineDispatcher
) : DownloadTwibbonUseCase{
    override suspend fun invoke(context: Context, bitmap: Bitmap, puzzleName: String, childNames: List<String>) {
        val fileName = "${puzzleName}-${childNames.joinToString()}"
        saveBitmapWithNotification(context, bitmap, fileName)
    }

    private suspend fun saveBitmapWithNotification(context: Context, bitmap: Bitmap, fileName : String) {
        withContext(dispatcher){
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val file = File(dir, fileName)

            var outputStream: OutputStream? = null

            try {
                outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()

                showNotification(context, file)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                outputStream?.close()
            }
        }
    }

    private fun showNotification(context: Context, file: File) {
        val notificationId = 14

        val intent = Intent(Intent.ACTION_VIEW)
        val uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".fileprovider",
            file
        )
        intent.setDataAndType(uri, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        notificationBuilder.setContentIntent(pendingIntent)
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

}