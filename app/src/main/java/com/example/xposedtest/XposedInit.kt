package com.example.xposedtest

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedInit : IXposedHookLoadPackage {
  @Throws(Throwable::class)
  override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    XposedBridge.log("Loaded app: " + lpparam.packageName)
    if (lpparam.packageName != "com.example.xposedtest")
      return
    XposedHelpers.findAndHookMethod("com.example.xposedtest.MainActivity", lpparam.classLoader, "onCreate", Bundle::class.java, object : XC_MethodHook() {
      @Throws(Throwable::class)
      override fun afterHookedMethod(param: XC_MethodHook.MethodHookParam?) {
        val cls = lpparam.classLoader.loadClass("com.example.xposedtest.MainActivity")
        val field = XposedHelpers.findField(cls, "tv")

        val tv = field.get(param!!.thisObject) as TextView

        tv.text = "X P O S E D\u3000I S\u3000R E A D Y !" /* show multi-space */
        tv.setTextColor(Color.RED)
      }
    })
  }
}
