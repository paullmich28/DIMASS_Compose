package com.ikanBilis.dimass.model

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ikanBilis.dimass.R

var channelID = System.currentTimeMillis().toString()
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationID = intent.getIntExtra("notificationID", 0)
        Log.d("Lagi", "YA")
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(intent.getStringExtra(titleExtra))
            .setContentText(intent.getStringExtra(messageExtra))
            .setGroup("group_notification")
            .build()

        manager.notify(notificationID, notification)
    }
}