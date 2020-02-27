package com.example.xposedtest.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.example.xposedtest.R
import com.example.xposedtest.receiver.DaemonReceiver

class DaemonService : Service() {

  companion object {
    val NOTICE_ID = 1000

    val defaultChannel: NotificationChannel by lazy {
      NotificationChannel("x001", "Message Notification", NotificationManager.IMPORTANCE_DEFAULT)
          .apply { description = "New message notifications" }
    }

    fun getNotificationBuilder(context: Context, manager: NotificationManager): Notification.Builder {
      return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
        manager.createNotificationChannel(defaultChannel)
        Notification.Builder(context, defaultChannel.id)
      } else {
        Notification.Builder(context)
      }
    }
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
    val builder = getNotificationBuilder(this, notificationManager)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentTitle("Keep Alive")
        .setContentText("Daemon service is running...")
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
    sendBroadcast(Intent().apply {
      action = "restart_service"
      setClass(this@DaemonService, DaemonReceiver::class.java)
    })
  }
}