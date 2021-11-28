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
import com.s24083.shoppinglistnotificator.services.ShoppingItemsService

class AddItemBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val binder = peekService(context, Intent(context, ShoppingItemsService::class.java))
            ?: return

        val service = (binder as ShoppingItemsService.MyBinder).getService()
        service.sentNewItemNotification(
            intent.getIntExtra("id", 0),
            intent.getStringExtra("name") ?: "")
    }
}