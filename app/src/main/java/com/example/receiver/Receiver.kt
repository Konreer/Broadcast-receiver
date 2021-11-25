package com.example.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Receiver(private val mainActivity: MainActivity) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(this::class.java.name, "Triggered!")
        Log.i(this::class.java.name,
            "Data received from previous receiver: " +
                    "${getResultExtras(true).getString("result")}"
        )
        mainActivity.createNotification(intent, context)

    }
}