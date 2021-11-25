package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*
import android.content.ComponentName
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK


class NotificationService : Service() {

    private lateinit var mBinder: MyBinder
    private var id = 0
    var requestCode = 1

    init {
        mBinder = MyBinder()
    }

    inner class MyBinder : Binder() {
        fun getService(): NotificationService = this@NotificationService
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }

    fun createNotification(intent: Intent, context: Context) {
        createNotificationChannel()

        intent.component = ComponentName(intent.extras?.getString("package")!!, intent.extras?.getString("class")!!)
        var ban = intent.extras?.getString("itemId")

        val pendingIntent = PendingIntent.getActivity(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(
            this,
            getString(R.string.channelID)
        )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText("Item " +
                    intent.extras?.getString("name")!! + " " +
                    intent.extras?.getInt("amount")!! + " " +
                    intent.extras?.getDouble("price")!! +
                    " was added")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .addAction(R.mipmap.ic_launcher, "Open app", pendingIntent)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(id++, notification)
        requestCode++
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
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