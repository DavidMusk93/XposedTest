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

class DaemonService : Service() {

  companion object {
    val NOTICE_ID = 100
  }

  private val TAG: String = "DaemonService"

  private lateinit var notificationManager: NotificationManager

  override fun onBind(intent: Intent?): IBinder? {
    return null
  }

  override fun onCreate() {
    super.onCreate()
    Log.d(TAG, "onCreate")
    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
      val channel = NotificationChannel("sun@1", "fake", NotificationManager.IMPORTANCE_DEFAULT)
      channel.description = "a trivial channel"
      notificationManager.createNotificationChannel(channel)
    }
    val builder = Notification.Builder(this, "sun@1")
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("fake title")
        .setContentText("test only")
    startForeground(NOTICE_ID, builder.build())
    startService(Intent(this, CancelNoticeService::class.java))
  }

  override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
    return START_STICKY
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d(TAG, "onDestroy")
    notificationManager.cancel(NOTICE_ID)
    val intent = Intent(applicationContext, DaemonService::class.java)
    startService(intent)
  }
}