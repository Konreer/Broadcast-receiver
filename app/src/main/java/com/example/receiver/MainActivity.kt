package com.example.receiver

import android.content.*
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val ACTION_ITEM_ADDED = "com.example.smb1.Activity.ItemEditActivity.ItemAdded"

    private lateinit var mserv: NotificationService
    private var mBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val filter = IntentFilter("com.example.smb1.Activity.ItemEditActivity.ITEMADDED")
        registerReceiver(Receiver(this), filter)
        setContentView(R.layout.activity_main)

    }

    fun createNotification(intent: Intent, context: Context){
        mserv.createNotification(intent, context)
    }

    val mcom = object: ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as NotificationService.MyBinder
            mserv = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mBound = false
        }

    }

    override fun onStart() {
        super.onStart()
        val intent = Intent(this, NotificationService::class.java)
        bindService(intent, mcom, Context.BIND_AUTO_CREATE)
    }

    override fun onStop() {
        super.onStop()
        if(mBound){
            unbindService(mcom)
            mBound = false
        }
    }
}