package com.s24083.shoppinglistnotificator.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.s24083.shoppinglistnotificator.R

class AddItemBroadcastReceiver : BroadcastReceiver() {

    var id = 0

    override fun onReceive(context: Context, intent: Intent) {
        var builder = NotificationCompat.Builder(context, context.getString(R.string.channelID))
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New shopping item")
            .setContentText("New shopping item has been created")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id++, builder.build())
        }
    }
}