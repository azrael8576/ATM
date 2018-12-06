package com.alex.atm.Bean

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import android.util.Log


class HelloService : IntentService("HelloService") {

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: ")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.d(TAG, "onHandleIntent: " + intent!!.getStringExtra("NAME"))
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val done = Intent()
        done.action = ACTION_HELLO_DONE
        sendBroadcast(done)
    }

    companion object {
        val ACTION_HELLO_DONE = "action_hello_done"
        private val TAG = HelloService::class.java.getSimpleName()
    }
}
