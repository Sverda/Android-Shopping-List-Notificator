package com.s24083.shoppinglistnotificator.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.s24083.shoppinglistnotificator.R

class ShoppingItemsService : Service() {

    private var id = 0
    private val requestCode = 1

    private val mBinder: MyBinder

    init{
        mBinder = MyBinder()
    }

    inner class MyBinder : Binder() {
        fun getService(): ShoppingItemsService = this@ShoppingItemsService
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    fun sentNewItemNotification(itemId: Int, itemName: String) {
        val editItemIntent = Intent("com.s24083.shoppinglist.ITEM_ADDED_RESPONSE")
        editItemIntent.putExtra("id", itemId)
        val pendingIntent = PendingIntent.getBroadcast(this, requestCode, editItemIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        var builder = NotificationCompat.Builder(this, getString(R.string.channelID))
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("New shopping item $itemName")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .addAction(R.mipmap.ic_launcher, "Edit", pendingIntent)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(id++, builder.build())
        }
    }
}