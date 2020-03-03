package com.example.xposedtest

import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.utility.callMethod
import com.example.xposedtest.xposed.HookSet
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage {

  private val hookClassMap = mutableMapOf<Int, Class<*>>().apply {
    HookSet.forEach {
      it.getAnnotation(HookClass::class.java)?.let { clz ->
        this[clz.pkg.hashCode()] = it
      }
    }
  }

  override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    val hash = lpparam.packageName.hashCode()
    if (hookClassMap.contains(hash))
      XposedHelpers.newInstance(hookClassMap[hash], lpparam)
          .callMethod("setupHook")
  }
}
