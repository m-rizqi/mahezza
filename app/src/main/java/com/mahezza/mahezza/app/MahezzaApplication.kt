package com.mahezza.mahezza.app

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.mahezza.mahezza.R
import com.mahezza.mahezza.di.NotificationModule
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber

@HiltAndroidApp
class MahezzaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createMahezzaNotification(applicationContext)
        Timber.plant(Timber.DebugTree())
    }

    fun createMahezzaNotification(
        @ApplicationContext
        context: Context
    ) {
        if (isAndroidVersionGreaterThanO()){
            val playSessionChannel = NotificationChannel(
                NotificationModule.PLAY_SESSION_NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.play_session_notification_channel),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(playSessionChannel)
        }
    }

    private fun isAndroidVersionGreaterThanO() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
}