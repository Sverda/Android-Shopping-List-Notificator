package com.s24083.shoppinglistnotificator.receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.s24083.shoppinglistnotificator.R

class AddItemBroadcastReceiver : BroadcastReceiver() {

    private var id = 0
    private val requestCode = 1

    override fun onReceive(context: Context, intent: Intent) {
        val editItemIntent = Intent("com.s24083.shoppinglist.ITEM_ADDED_RESPONSE")
        editItemIntent.putExtra("id", intent.getIntExtra("id", 0))
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, editItemIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(context, context.getString(R.string.channelID))
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New shopping item ${intent.getStringExtra("name")}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.mipmap.ic_launcher, "Edit", pendingIntent)
        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(id++, builder.build())
        }
    }
}