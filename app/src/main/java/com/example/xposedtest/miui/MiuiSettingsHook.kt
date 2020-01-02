package com.example.xposedtest.miui

import android.preference.Preference
import android.preference.PreferenceActivity
import com.example.xposedtest.utility.DebugUtil
import com.example.xposedtest.utility.cast
import de.robv.android.xposed.XC_MethodHook

object MiuiSettingsHook {

  val onPreferenceTreeClick = object : XC_MethodHook() {

    fun xLog(msg: String?) = DebugUtil.log("[Click]$msg")

    override fun beforeHookedMethod(param: MethodHookParam) {
      super.beforeHookedMethod(param)
      param.args[1].cast<Preference>()?.run { xLog("${param.thisObject}#${this.key}") }
    }
  }

  val onHeaderClick = object : XC_MethodHook() {

    fun xLog(msg: String?) = DebugUtil.log("[onHeaderClick]$msg")

    override fun beforeHookedMethod(param: MethodHookParam) {
      super.beforeHookedMethod(param)
      param.args[0].cast<PreferenceActivity.Header>()?.run {
        xLog("${this.id} | ${param.args[1]} | ${this.fragment} | ${this.intent} | ${this.fragmentArguments} | ${this.titleRes}")
      }
    }
  }

}
