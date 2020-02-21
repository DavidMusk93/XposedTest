package com.example.xposedtest.module.miui

import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class UpdaterHook(lppram: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lppram, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookRequest() {
    "$packageName.utils.n".`class`()!!
        .apply {
          hook("gz", C.String, C.HashMap, C.ArrayMap, C.String,
              hookAfter {
                //log("Request", args[0], result)
                if ("${args[0]}".contains("update.miui.com")) {
                  result = null
                }
              })
        }
  }
}
