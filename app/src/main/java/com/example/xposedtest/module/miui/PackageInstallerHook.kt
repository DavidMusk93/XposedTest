package com.example.xposedtest.module.miui

import android.content.pm.PackageInfo
import android.view.View
import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.*
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

@HookClass("com.miui.packageinstaller")
class PackageInstallerHook(lpparam: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lpparam, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookInstall() {
    "com.android.packageinstaller.PackageInstallerActivity".`class`()!!.apply {
      val nameOfPackageInfoField = this.fieldName(C.PackageInfo) ?: return
      hook("onResume", hookAfter {
        log("Resume", "($appVersion)Does uri parsing finished?")
        thisObject.getField<PackageInfo>(nameOfPackageInfoField)?.apply {
          log("PackageInstaller", packageName)
        }
      })

      val nameOfSecondButtonField = this.fieldName(C.Button, 2) ?: return
      hook("onClick", C.View, hookBefore {
        if ("${args[0]}".contains(Regex("ok|install"))) {
          var cancelButton: Any? = null
          kotlin.runCatching {
            cancelButton = thisObject.getField("mCancel")
          }.onFailure { cancelButton = thisObject.getField(nameOfSecondButtonField) }
          cancelButton ?: return@hookBefore
          val pkg = thisObject.getField<PackageInfo>(nameOfPackageInfoField)?.packageName
          if (Config.Package.preventInstallAppList.contains("$pkg")) {
            args[0] = cancelButton
          }
        }
      })
    }
  }

  @HookMethod
  private fun hookUninstall() {
    "com.android.packageinstaller.UninstallerActivity".`class`()!!.apply {
      val nameOfPackageNameField = this.fieldName(C.String) ?: return
      hook("onClick", C.View, hookBefore {
        if ("${args[0]}".contains("continue")) {
          val pkg = thisObject.getField<String>(nameOfPackageNameField) ?: return@hookBefore
          if (Config.Package.protectAppList.contains(pkg)) {
            val id = args[0].cast<View>()?.id ?: return@hookBefore
            args[0] = thisObject.callMethod("findViewById", id - 1)
          }
        }
      })
    }
  }
}