package com.example.xposedtest

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.lang.ref.WeakReference

class LaunchAppViaDialReceiver : BroadcastReceiver() {
  val LAUNCHER_COMPONENT_NAME = ComponentName("com.example.xposedtest", "com.example.xposedtest.MainActivity")
  var mContextRef: WeakReference<Context>? = null

  private fun isLauncherIconVisible(): Boolean {
    val enableSetting = mContextRef?.get()?.packageManager?.getComponentEnabledSetting(LAUNCHER_COMPONENT_NAME)
    if (enableSetting != null) {
      return enableSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED
    }
    return false
  }

  override fun onReceive(context: Context, intent: Intent) {
    mContextRef = WeakReference(context)
    val bundle = intent.extras
    bundle ?: return

    val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
    if (phoneNumber.equals("1234#")) {
      resultData = null

      if (!isLauncherIconVisible()) {
        mContextRef?.get()?.packageManager?.setComponentEnabledSetting(LAUNCHER_COMPONENT_NAME, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP)
      }

      val appIntent = Intent(context, MainActivity::class.java)
      appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
      context.startActivity(appIntent)
    }
  }
}