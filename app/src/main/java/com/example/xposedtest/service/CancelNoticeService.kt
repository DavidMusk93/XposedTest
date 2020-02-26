package com.example.xposedtest.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.xposedtest.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CancelNoticeService : Service() {

  private lateinit var notificationManager: NotificationManager

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      val channel = NotificationChannel("sun@2", "fake2", NotificationManager.IMPORTANCE_DEFAULT)
      channel.description = "a trivial channel2"
      notificationManager.createNotificationChannel(channel)
    }
    val builder = Notification.Builder(this, "sun@2")
        .setSmallIcon(R.mipmap.ic_launcher)
    startForeground(DaemonService.NOTICE_ID, builder.build())
    GlobalScope.launch { stop() }
  }

  private suspend fun stop() {
    delay(1000)
    kotlin.runCatching {
      Log.i("CancelNoticeService", "stop service")
      stopForeground(true)
      notificationManager.cancel(DaemonService.NOTICE_ID)
      stopSelf()
    }.onFailure { Log.w("CancelNoticeService", "Failure:$it") }
  }
}