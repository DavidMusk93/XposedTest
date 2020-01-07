package com.example.xposedtest.shyd

import android.content.Context
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ShydHookContext: HookContext() {

  private fun hookTrivial() {

  }

  override fun attachBaseContext() {
    attach("com.secneo.apkwrapper.ApplicationWrapper")
  }

  override fun hook() {
    super.hook()
    hookTrivial()
  }
}

class ShydHook(param: XC_LoadPackage.LoadPackageParam)
  : HookEntry(param, ShydHookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private fun hook() {
    "com.secneo.apkwrapper.d".`class`()!!.apply {
      hook("b", hookAfter { log("(b)Result", result) })
      hook("onEvent",
          C.Int, C.String,
          hookAfter { log("onEvent", *args) })
    }
  }
}