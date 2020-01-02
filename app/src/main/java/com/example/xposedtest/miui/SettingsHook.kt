package com.example.xposedtest.miui

import android.os.Bundle
import android.preference.Preference
import com.example.xposedtest.utility.*
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

class SettingsHook(lpparam: XC_LoadPackage.LoadPackageParam): HookEntry(lpparam, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private fun hook() {
    // val cs = MiuiSettingsClass
    // cs.MiuiMyDeviceSettings.`class`()!!.hook("onPreferenceTreeClick", C.PreferenceScreen, C.Preference, MiuiSettingsHook.onPreferenceTreeClick)
    // cs.MiuiSettings.`class`()!!.hook("onHeaderClick", C.Header, C.Int, MiuiSettingsHook.onHeaderClick)

    val context = UpgradeContext()

    "${Settings()["device"][if (is_N()) "s" else "r"]}".`class`()!!
        .hook("onClick",
            C.View,
            hookBoth({
              thisObject.getField<Any>(context.s1)
                  ?.run {
                    val b = getField<Boolean>(context.s2)?: return@run
                    if (b) {
                      setField(context.s2, false)
                      context.obj = this
                      "Upgrade is disabled!".toast()
                    }
                  }
            }, {
              context.obj
                  ?.run { setField(context.s2, true) }
            }))

    "${Settings()["development"]["DevelopmentSettings"]}".`class`()!!.let { clazz ->
      clazz.hook("H",
          C.Preference,
          hookBefore {
            // log("CheckInstance", *args, thisObject.getField("awK"))
            log("DevelopmentSettings", args[0].cast<Preference>()?.key)
          })

      // clazz.hook("onCreate",
      //     C.Bundle,
      //     hookAfter {
      //       XposedHelpers.callMethod(thisObject, "findPreference", "debug_debugging_category")
      //           ?.apply {
      //             log("removePreference", "awJ")
      //             XposedHelpers.callMethod(this, "removePreference", thisObject.getField("awJ"))
      //           }
      //       XposedHelpers.callMethod(thisObject, "startActivityForResult", Intent("com.miui.securitycenter.action.ADB_INSTALL_VERIFY"), 11)
      //     })
    }


    val a = "${Settings.android["a"]["a"]}".`class`()!!

    a.hook("cEh",
        C.Context,
        hookBoth({
          log("MethodInvoke", "cEh")
        }, {
          val b = result.cast<Boolean>()?: return@hookBoth
          if (!b) {
            log("Enable", "adb install")
            Bundle().apply {
              putInt("type", 1)
              putString("key", "security_adb_install_enable")
              putBoolean("value", true)
              XposedHelpers.callStaticMethod(a, "cEe", this@hookBoth.args[0], "SET", this)
            }
            result = true
          }
        }))

    a.hook("cEg",
        hookBoth({
          log("MethodInvoke", "cEg")
        }, {
          val b = result.cast<Boolean>()?: return@hookBoth
          if (!b) {
            log("Enable", "adb input")
            XposedHelpers.callStaticMethod(a, "cEi", true)
            result = true
          }
        }))

  }

}
