package com.example.xposedtest.module.miui

import android.content.pm.PackageInfo
import android.view.View
import android.widget.Button
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.callMethod
import com.example.xposedtest.utility.cast
import com.example.xposedtest.utility.getField
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class PackageInstallerHook(lpparam: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lpparam, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookInstall() {
    "com.android.packageinstaller.PackageInstallerActivity".`class`()!!
        .apply {
          hook("onResume",
              hookAfter {
                log("Resume", "Does uri parsing finished?")
                thisObject.getField<PackageInfo>("mPkgInfo")?.apply {
                  log("PackageInstaller", packageName)
                }
              })

          hook("onClick", C.View,
              hookBefore {
                log("View", *args)
                val okButtion = thisObject.getField<Button>("mOk") ?: return@hookBefore
                val pkg = thisObject.getField<PackageInfo>("mPkgInfo")?.packageName
                if (Config.Package.preventInstallAppList.contains("$pkg")) {
                  if (args[0] == okButtion) {
                    args[0] = thisObject.getField("mCancel")
                  }
                }
              })
        }
  }

  @HookMethod
  private fun hookUninstall() {
    "com.android.packageinstaller.UninstallerActivity".`class`()!!
        .apply {
          hook("onClick", C.View,
              hookBefore {
                log("Click", *args)
                val pkg = thisObject.getField<String>("mPackageName") ?: return@hookBefore
                if (Config.Package.protectAppList.contains(pkg)) {
                  val id = args[0].cast<View>()?.id ?: return@hookBefore
                  if (id.and(1) == 1) {
                    args[0] = thisObject.callMethod("findViewById", id - 1)
                  }
                }
              })
        }
  }
}