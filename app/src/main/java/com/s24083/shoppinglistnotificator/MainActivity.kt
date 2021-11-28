package com.s24083.shoppinglistnotificator

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.*
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationManagerCompat
import com.s24083.shoppinglistnotificator.databinding.ActivityMainBinding
import com.s24083.shoppinglistnotificator.receivers.AddItemBroadcastReceiver
import com.s24083.shoppinglistnotificator.services.ShoppingItemsService

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var mserv: ShoppingItemsService
    private var mBound: Boolean = false

    private val mcom = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ShoppingItemsService.MyBinder
            mserv = binder.getService()
            mBound = true
        }
        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val receiver = AddItemBroadcastReceiver()
        val filter = IntentFilter()
        filter.addAction("com.s24083.shoppinglist.ITEM_ADDED")
        registerReceiver(receiver, filter)
        createNotificationChannel()

        val intent = Intent(this, ShoppingItemsService::class.java)
        startService(intent)
        bindService(intent, mcom, Context.BIND_AUTO_CREATE)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
            mserv.sentNewItemNotification(1, "hey")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mBound){
            unbindService(mcom)
            mBound = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return
        // SDK >= 26
        val notificationChannel = NotificationChannel(
            getString(R.string.channelID),
            getString(R.string.channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationChannel.description =
            getString(R.string.channel_description)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}