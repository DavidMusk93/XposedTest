package com.example.xposedtest.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.xposedtest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CancelNoticeService : Service() {

  private val TAG = "CancelNoticeService"

  private lateinit var notificationManager: NotificationManager

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    Log.i(TAG, "onCreate")
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val builder = DaemonService.getNotificationBuilder(this, notificationManager)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Cancel Notice")
        .setContentText("Disappear after 1 second")
        .setAutoCancel(true)
    startForeground(DaemonService.NOTICE_ID, builder.build())
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    Log.i(TAG, "onStartCommand")
    CoroutineScope(Dispatchers.Unconfined).launch { stop() }
    return super.onStartCommand(intent, flags, startId)
  }

  private suspend fun stop() {
    delay(1000)
    kotlin.runCatching {
      Log.i(TAG, "Stop service")
      stopForeground(true)
      notificationManager.cancel(DaemonService.NOTICE_ID)
      stopSelf()
    }.onFailure { Log.w("CancelNoticeService", "Failure:$it") }
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.i(TAG, "onDestroy")
  }
}