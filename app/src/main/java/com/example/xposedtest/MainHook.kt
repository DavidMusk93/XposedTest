package com.example.xposedtest

import com.example.xposedtest.utility.callMethod
import com.example.xposedtest.xposed.HookMap
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {

  override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    kotlin.runCatching {
      HookMap[lpparam.packageName.hashCode()]
          ?.let { XposedHelpers.newInstance(it, lpparam).callMethod("setupHook") }
    }.onFailure { it.printStackTrace() }
  }
}
