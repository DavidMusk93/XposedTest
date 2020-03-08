package com.example.xposedtest.module.miui

import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceScreen
import android.view.Menu
import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.extension.bool
import com.example.xposedtest.utility.*
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

  private val a = "${Settings.android["a"]["a"]}".`class`()!!
  private val nameOfAdbInstallCheckMethod = a.methodName(C.Boolean, C.Context)
  private val nameOfSetSecurityPreferenceMethod = a.methodName(C.Bundle, C.Context, C.String, C.Bundle)
  private val nameOfSetInputEnabledMethod = a.methodName(C.Void, C.Boolean)

  private fun enableAdbInstall(clz: Class<*>, ctx: Any, m1: String, m2: String) {
    Bundle().apply {
      putInt("type", 1)
      putString("key", "security_adb_install_enable")
      putBoolean("value", true)
      clz.callStaticMethod(m1, ctx, "SET", this)
    }
    clz.callStaticMethod(m2, true)
  }

  @HookMethod
  private fun hookDevelopmentSetting() {
    nameOfAdbInstallCheckMethod ?: return
    nameOfSetSecurityPreferenceMethod ?: return
    nameOfSetInputEnabledMethod ?: return
    a.apply {
      hook(nameOfAdbInstallCheckMethod, C.Context, hookAfter {
        log("DevelopmentSetting", "enable adb_install & adb_input")
        if (result.bool()) return@hookAfter
        enableAdbInstall(a, args[0], nameOfSetSecurityPreferenceMethod, nameOfSetInputEnabledMethod)
        result = true
      })
    }

    /** HOOK TIPS: It is feasible to hook default method of abstract class. */
    //"$packageName.dashboard.DashboardFragment".`class`()!!.apply {
    //  hook("onAttach", C.Context,
    //      hookBefore {
    //        kotlin.runCatching {
    //          log("onAttach", *args, thisObject.callMethod("mI"))
    //        }.onFailure { log("hookDevelopmentSetting@Failure", it) }
    //      })
    //}

    kotlin.runCatching {
      "$packageName.development.DevelopmentSettingsDashboardFragment".`class`()!!.apply {
        hook("onActivityCreated", C.Bundle, hookAfter {
          a.callStaticMethod(nameOfAdbInstallCheckMethod, thisObject.callMethod("getContext"))
        })
      }
    }.onFailure { it.printStackTrace() }
  }

}
