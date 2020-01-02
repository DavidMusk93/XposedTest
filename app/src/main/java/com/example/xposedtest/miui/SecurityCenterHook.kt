package com.example.xposedtest.miui

import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SecurityCenterHook(lpparam: XC_LoadPackage.LoadPackageParam): HookEntry(lpparam, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private fun hook() {
    "com.miui.common.persistence.RemoteProvider".`class`()!!.hook("hD",
        C.String, C.Boolean,
        hookBefore {
          val k = args[0].cast<String>()?: return@hookBefore
          if (k.equals("security_adb_install_enable"))
            args[1] = false
          log("ModifyArg", "enable adb install")
        })
  }
}