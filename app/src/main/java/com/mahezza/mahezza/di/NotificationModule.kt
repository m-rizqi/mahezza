package com.mahezza.mahezza.di

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mahezza.mahezza.R
import com.mahezza.mahezza.ui.features.game.playsession.service.PlaySessionServiceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PlaySessionNotificationBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DownloadImageNotificationBuilder

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {
    const val PLAY_SESSION_NOTIFICATION_CHANNEL_ID = "play_session"
    const val DOWNLOAD_IMAGE_NOTIFICATION_CHANNEL_ID = "download_image"

    @Provides
    @PlaySessionNotificationBuilder
    fun providePlaySessionNotificationBuilder(
        @ApplicationContext
        context: Context
    ): NotificationCompat.Builder {
        val notificationBuilder = NotificationCompat.Builder(context, PLAY_SESSION_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mahezza_launcher_foreground)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .addAction(PlaySessionServiceHelper.getPauseNotificationAction(context))
            .setContentIntent(null)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationBuilder.priority = NotificationManager.IMPORTANCE_DEFAULT
        }
        return notificationBuilder
    }

    @Provides
    @DownloadImageNotificationBuilder
    fun provideDownloadImageNotificationBuilder(
        @ApplicationContext
        context: Context
    ): NotificationCompat.Builder {
        val notificationBuilder = NotificationCompat.Builder(context, PLAY_SESSION_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_mahezza_launcher_foreground)
            .setContentTitle(context.getString(R.string.download_image))
            .setContentText(context.getString(R.string.tap_to_view))
            .setAutoCancel(true)

        return notificationBuilder
    }

    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}