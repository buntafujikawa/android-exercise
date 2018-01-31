package com.example.buntafujikawa.rssreader

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat

/**
 * 定型の通知を送る
 */
class NotificationUtil {
    companion object {
        private const val REQUEST_MAIN_ACTIVITY: Int = 1

        fun notifyUpdate(context: Context, newLinks: Int) {
            // 通知タップでアクティビティを開く
            val intent: Intent = Intent(context, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            val viewIntent: PendingIntent = PendingIntent.getActivity(context, REQUEST_MAIN_ACTIVITY, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // 通知オブジェクトを作る
            val notification: Notification = NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(
                    R.string.notification_message, newLinks))
                .setContentIntent(viewIntent)
                .setAutoCancel(true) // タップすると通知が消える
                .build()

            val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // 通知する
            notificationManager.notify(REQUEST_MAIN_ACTIVITY, notification)
        }
    }
}
