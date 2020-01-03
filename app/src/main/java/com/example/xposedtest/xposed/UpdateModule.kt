package com.example.xposedtest.xposed

import android.app.Activity
import android.app.AlertDialog
import com.example.xposedtest.R
import com.example.xposedtest.utility.is_N
import com.topjohnwu.superuser.Shell

object UpdateModule {

  val XPOSED_MODULE_LIST by lazy {
    if (is_N())
      "/data/user_de/0/de.robv.android.xposed.installer/conf/modules.list"
    else
      "/data/user_de/0/org.meowcat.edxposed.manager/conf/modules.list"
  }

  fun update() {
    val appDirectoryPrefix = "/data/app/${com.example.xposedtest.BuildConfig.APPLICATION_ID}"
    val modules = Shell.su("cat $XPOSED_MODULE_LIST").exec()
        .out
        .filter { !it.startsWith(appDirectoryPrefix) }
        .toMutableList()
    Shell.su("echo $appDirectoryPrefix*/base.apk").exec()
        .out
        .forEach { modules.add(it) }
    Shell.su("echo ${modules.joinToString("\n")} > $XPOSED_MODULE_LIST").exec()
  }

  fun reboot(activity: Activity) {
    AlertDialog.Builder(activity).apply {
      setMessage(R.string.reboot_prompt)
      setPositiveButton(R.string.ensure_reboot) { dialog, which -> Shell.su("reboot").exec() }
      setNegativeButton(R.string.cancel_reboot, null)
      show()
    }
  }
}