package com.example.xposedtest.xposed

import android.app.Activity
import android.app.AlertDialog
import com.example.xposedtest.R
import com.example.xposedtest.utility.is_N
import com.topjohnwu.superuser.Shell

object UpdateModule {

  val XLIST by lazy {
    if (is_N())
      "/data/user_de/0/de.robv.android.xposed.installer/conf/modules.list"
    else
      "/data/user_de/0/org.meowcat.edxposed.manager/conf/modules.list"
  }

  fun update(sourceDir: String) {
    val appDirectoryPrefix = "/data/app/${com.example.xposedtest.BuildConfig.APPLICATION_ID}-"
    var modules = Shell.su("cat $XLIST").exec()
        .out
        .filter { !it.startsWith(appDirectoryPrefix) }
        .joinToString(" ")
    // Shell.su("echo $appDirectoryPrefix*/base.apk").exec()
    //     .out
    //     .forEach { modules.add(it) }
    modules += " $sourceDir"

    // Shell.su("echo ${modules.joinToString("\n")} > $XLIST").exec()
    Shell.su(">$XLIST && for i in $modules; do echo \$i >> $XLIST; done").exec()
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