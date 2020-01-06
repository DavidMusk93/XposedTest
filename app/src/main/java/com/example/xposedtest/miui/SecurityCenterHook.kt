package com.example.xposedtest.miui

import android.widget.Button
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.utility.getField
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.*

class SecurityCenterHook(lpparam: XC_LoadPackage.LoadPackageParam) : HookEntry(lpparam, HookContext()), IHookEntry {

  private val responseTimePool = listOf<Long>(500, 1000, 1500, 2000, 2500, 3000)

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private fun hook() {
    "${SecurityCenter.RemoteProvider}".`class`()!!.hook("hD",
        C.String, C.Boolean,
        hookBefore {
          val k = args[0].cast<String>() ?: return@hookBefore
          if (k.equals("security_adb_install_enable"))
            args[1] = false
          log("BlockPermissionCheck", "enable adb install")
        })

    "${SecurityCenter.AdbInstallActivity}".`class`()!!.hook("Kt",
        C.View,
        hookAfter {
          val obj = thisObject.getField<Any>("BT")
          // In kotlin, it is really hard to represent `Class[]`
          // val clsArr: Array<Class<*>> = Array(1, {IntArray::class.java.componentType})
          // val cls = Class.forName("com.android.internal.app.AlertController")
          var acceptButton: Button? = null
          runCatching {
            // log("Button(-1)", XposedHelpers.callStaticMethod("${SecurityCenter.a}".`class`(), "bwk", obj, "getButton", cls, clsArr, -1))
            // log("Button(-2)", XposedHelpers.callStaticMethod("${SecurityCenter.a}".`class`(), "bwk", obj, "getButton", cls, clsArr, -2))
            log("Button(-1)", XposedHelpers.callMethod(obj, "getButton", -1)) // Same as Button `Ca`
            log("Button(-2)", XposedHelpers.callMethod(obj, "getButton", -2)
                .also {
                  it
                      .cast<Button>()
                      ?.apply { acceptButton = this }
                })
            log("Button(Ca)", thisObject.getField("Ca"))
          }.onFailure { log("Exception", "${it.message}") }
          CoroutineScope(Dispatchers.Default).launch {
            delay(responseTimePool.shuffled().last())
            MainScope().launch { acceptButton?.performClick() }
          }
        })
  }

}