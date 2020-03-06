package com.example.xposedtest.module.miui

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceScreen
import android.view.Menu
import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.callStaticMethod
import com.example.xposedtest.utility.cast
import com.example.xposedtest.utility.methodName
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage

@HookClass("com.android.settings")
class SettingsHook(lpparam: XC_LoadPackage.LoadPackageParam) : HookEntry(lpparam, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookReset() {
    val ClearFragment = "com.android.settings.MiuiMasterClear"

    "com.android.settings.PrivacySettings".`class`()!!
        .apply {
          hook("onPreferenceTreeClick", C.PreferenceScreen, C.Preference,
              hookBefore {
                log("Reset@Key", *args, args[1].cast<Preference>()?.key)
              })

          hook("onCreate", C.Bundle,
              hookAfter {
                XposedHelpers.callMethod(thisObject, "getPreferenceScreen")
                    .cast<PreferenceScreen>()
                    ?.apply {
                      val count = preferenceCount
                      log("PrefScreen@count", count)
                      (0 until count).reversed().forEach() {
                        val preference = getPreference(it)
                        if ("${preference.fragment}".equals(ClearFragment)) {
                          removePreference(preference)
                          return@forEach
                        }
                      }
                    }
              })
        }

    ClearFragment.`class`()!!.apply {
      hook("onCreateOptionsMenu", C.Menu, C.MenuInflater, hookAfter {
        /* Difference with getItem? */
        args[0].cast<Menu>()?.findItem(1)?.isVisible = false
      })
    }
  }

  @HookMethod
  private fun hook() {
    val a = "${Settings.android["a"]["a"]}".`class`()!!
    a.apply {
      val nameOfAdbInstallCheckMethod = this.methodName(C.Boolean, C.Context) ?: return
      val nameOfSetSecurityPreferenceMethod = this.methodName(C.Bundle, C.Context, C.String, C.Bundle)
          ?: return
      val nameOfSetInputEnabledMethod = this.methodName(C.Void, C.Boolean) ?: return
      log("MethodName", nameOfAdbInstallCheckMethod, nameOfSetSecurityPreferenceMethod, nameOfSetInputEnabledMethod)
      hook(nameOfAdbInstallCheckMethod, C.Context, hookAfter {
        log("DevelopmentSetting", "enable adb_install & adb_input")
        val r = result.cast<Boolean>() ?: return@hookAfter
        if (r) return@hookAfter
        Bundle().apply {
          putInt("type", 1)
          putString("key", "security_adb_install_enable")
          putBoolean("value", true)
          a.callStaticMethod(nameOfSetSecurityPreferenceMethod, this@hookAfter.args[0], "SET", this)
        }
        a.callStaticMethod(nameOfSetInputEnabledMethod, true)
        result = true
      })
    }

  }

}
