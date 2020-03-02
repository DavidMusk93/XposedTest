package com.example.xposedtest.utility

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import com.example.xposedtest.R
import com.example.xposedtest.extension.toast
import com.topjohnwu.superuser.Shell
import java.io.File

object ModuleHelper {

  val XLIST by lazy {
    if (is_N())
      "/data/user_de/0/de.robv.android.xposed.installer/conf/modules.list"
    else
      "/data/user_de/0/org.meowcat.edxposed.manager/conf/modules.list"
  }

  fun updateList(context: Context) {
    if (isSuReady()) {
      updateXposedList(context.applicationInfo.sourceDir)
    } else {
      "Su not ready!".toast(context)
    }
    updateSelfList(context)
  }

  fun updateXposedList(sourceDir: String) {
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
    Shell.su("test -f $XLIST && >$XLIST && for i in $modules; do echo \$i >> $XLIST; done").exec()
  }

  fun updateSelfList(context: Context) {
    val writer = File(FsUtil.XPOSED_CONF + "/modules.list").printWriter()
    context.packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
        .forEach {
          it.applicationInfo.apply {
            if (metaData != null && metaData.containsKey("xposedmodule")) {
              writer.println(sourceDir)
            }
          }
        }
    writer.close()
  }

  fun isSuReady() = Shell.su("ls /data/").exec().isSuccess

  fun reboot(activity: Activity) {
    AlertDialog.Builder(activity).apply {
      setMessage(R.string.reboot_prompt)
      setPositiveButton(R.string.ensure_reboot) { dialog, which -> Shell.su("reboot").exec() }
      setNegativeButton(R.string.cancel_reboot, null)
      show()
    }
  }
}