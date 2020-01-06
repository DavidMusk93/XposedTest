package com.example.xposedtest.tomato

import android.content.Context
import android.view.View
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.utility.getField
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class TomatoHookContext: HookContext() {

  private lateinit var realClassLoader: ClassLoader

  private fun String.`class`() = XposedHelpers.findClassIfExists(this, realClassLoader)

  private fun log(tag: String, vararg param: Any?) {
    gLog("@$processName@$tag", *param)
  }

  private fun hookTrivial() {
    "com.one.tomato.mvp.base.BaseApplication".`class`()
        .apply {
          hook("j",
              hookAfter {
                log("Field", thisObject.getField("b"))
                log("Field", thisObject.getField("d"))
              })

          hook("e", hookAfter { log("(e)Result", result) })
        }

    "com.one.tomato.ui.MainTabActivity".`class`()
        .apply {
          hook("onClick", C.View,
              hookBefore { log("onClick", args[0].cast<View>()?.id) })

          hook("k", C.Int,
              hookBefore { log("(k)Input", args[0], thisObject.getField("B")) })
        }
  }

  override fun attachBaseContext() {
    XposedHelpers.findAndHookMethod(
        "com.mine.proxy_core.ProxyApplication", classLoader,
        "attachBaseContext",
        C.Context,
        hookAfter {
          args[0].cast<Context>()
              ?.classLoader
              ?.apply {
                realClassLoader = this
                hookTrivial()
              }
        })

  }
}

class TomatoHook(param: XC_LoadPackage.LoadPackageParam)
  : HookEntry(param, TomatoHookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
  }
}