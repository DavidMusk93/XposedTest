package com.example.xposedtest.module.miui

import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MiuiHomeHook(lpparam: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lpparam, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookUnintall() {
    val protectAppList = listOf("com.example.xposedtest")

    val ShortcutInfo = "com.miui.home.launcher.ShortcutInfo".`class`()
    "com.miui.home.launcher.UninstallDialog".`class`()!!
        .apply {
          hook("deletePackage", C.String, ShortcutInfo,
              hookBefore {
                log("Uninstall", *args)
                if (protectAppList.contains("${args[0]}")) {
                  args[0] = null
                  args[1] = null
                }
              })
        }
  }
}