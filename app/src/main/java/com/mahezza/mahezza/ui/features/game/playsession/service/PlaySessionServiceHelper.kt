package com.mahezza.mahezza.ui.features.game.playsession.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mahezza.mahezza.R
import com.mahezza.mahezza.app.MainActivity

object PlaySessionServiceHelper {

    const val CLICK_REQUEST_CODE = 456
    const val PAUSE_REQUEST_CODE = 457
    const val RESUME_REQUEST_CODE = 458
    const val CANCEL_REQUEST_CODE = 459

    private val flag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            PendingIntent.FLAG_IMMUTABLE
        else
            0
    fun clickPendingIntent(context: Context): PendingIntent {
        val clickIntent = Intent(context, MainActivity::class.java).apply {
            putExtra(PlaySessionService.StopwatchState, PlaySessionService.StopwatchState.Started.name)
        }
        return PendingIntent.getActivity(
            context, CLICK_REQUEST_CODE, clickIntent, flag
        )
    }

    fun pausePendingIntent(context: Context): PendingIntent {
        val pauseIntent = Intent(context, PlaySessionService::class.java).apply {
            action = PlaySessionService.Actions.PAUSE.name
        }
        return PendingIntent.getService(
            context, PAUSE_REQUEST_CODE, pauseIntent, flag
        )
    }

    fun resumePendingIntent(context: Context): PendingIntent {
        val resumeIntent = Intent(context, PlaySessionService::class.java).apply {
            action = PlaySessionService.Actions.START.name
        }
        return PendingIntent.getService(
            context, RESUME_REQUEST_CODE, resumeIntent, flag
        )
    }

    fun finishedPendingIntent(context: Context): PendingIntent {
        val cancelIntent = Intent(context, PlaySessionService::class.java).apply {
            action = PlaySessionService.Actions.Finished.name
        }
        return PendingIntent.getService(
            context, CANCEL_REQUEST_CODE, cancelIntent, flag
        )
    }

    fun triggerForegroundService(context: Context, action: String) {
        Intent(context, PlaySessionService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }

    fun getPauseNotificationAction(context: Context) : NotificationCompat.Action = NotificationCompat.Action(
        0, context.getString(R.string.pause), pausePendingIntent(context)
    )

    fun getFinishedNotificationAction(context: Context) : NotificationCompat.Action = NotificationCompat.Action(
        0, context.getString(R.string.finished), finishedPendingIntent(context)
    )
    fun getResumeNotificationAction(context: Context) : NotificationCompat.Action = NotificationCompat.Action(
        0, context.getString(R.string.resume), resumePendingIntent(context)
    )
}