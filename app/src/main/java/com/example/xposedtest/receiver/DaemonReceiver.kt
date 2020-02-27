package com.example.xposedtest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.xposedtest.service.DaemonService

class DaemonReceiver : BroadcastReceiver() {

  override fun onReceive(context: Context, intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      context.startForegroundService(Intent(context, DaemonService::class.java))
    } else {
      context.startService(Intent(context, DaemonService::class.java))
    }
  }
}