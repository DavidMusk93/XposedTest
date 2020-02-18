package com.example.xposedtest.miui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.preference.Preference
import android.preference.PreferenceActivity
import android.widget.TextView
import com.example.xposedtest.MainHook
import com.example.xposedtest.utility.*
import com.example.xposedtest.utility.DebugUtil.Companion.flattenBundle
import com.example.xposedtest.xposed.hookAfter
import com.example.xposedtest.xposed.hookBefore
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import java.net.URLDecoder
import java.net.URLEncoder

object MiuiMarketHook {

  object Cache {
    var currentInstallPackage: String? = null
  }

  object Const {
    val disablePackageList = listOf("抖音短视频", "快手").map { URLEncoder.encode(it, "utf-8") }
  }

  fun log(tag: String, msg: String?) = DebugUtil.log("$tag#$msg")

  val onPreferenceClick = object : XC_MethodHook() {
    fun xLog(msg: String?) = DebugUtil.log("[onPreferenceClick]$msg")

    override fun beforeHookedMethod(param: MethodHookParam) {
      super.beforeHookedMethod(param)
      param.args[0].cast<Preference>()?.run { xLog(this.key) }
    }
  }

  val onPreferenceChange = object : XC_MethodHook() {
    fun xLog(msg: String?) = DebugUtil.log("[onPreferenceChange]$msg")

    override fun beforeHookedMethod(param: MethodHookParam) {
      super.beforeHookedMethod(param)
      param.args[0].cast<Preference>()?.run { xLog("${this.key} | ${param.args[1]}") }
    }
  }

  val preferenceForceFalse = object : XC_MethodHook() {
    fun xLog(msg: String?) = DebugUtil.log("[preferenceForceFalse]$msg")

    override fun afterHookedMethod(param: MethodHookParam) {
      xLog("${param.args[0]} | ${param.args[1]}")

      param.args[1].cast<Boolean>()?.run {
        if (this)
          XposedHelpers.callStaticMethod(MiuiMarketClass.fb, "a", param.args[0], !this)
      }
    }
  }

  val filterPreferenceList = hookAfter {
    result.cast<ArrayList<String>>()?.apply {
      for (pref in arrayOf("pref_key_update_notification", "pref_key_auto_update_via_wifi"))
        remove(pref)
    }
  }

  val forceReturnTrue = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
      param.result = true
    }
  }

  val disableAutoUpdateMarket = object : XC_MethodHook() {
    override fun afterHookedMethod(param: MethodHookParam) {
      param.thisObject.cast<PreferenceActivity>()?.run {
        val k = findPreference("pref_key_auto_update_market")
        preferenceScreen.removePreference(k)
      }
    }
  }

  val checkTextView = hookBefore {
    args[0].cast<TextView>()?.run { DebugUtil.log("[checkTextView]${this.text}") }
  }

  val peekString = hookAfter {
    args[0].cast<String>()?.run { DebugUtil.log("[peekString]$this") }
  }

  val UpdateAppsActivity_onCreate = hookAfter {
    thisObject.cast<Activity>()?.intent?.run {
      DebugUtil.log("[UpdateAppsActivity_onCreate]${flattenBundle(extras)}")
    }
  }

  val e_PeekIntent = hookAfter {
    args[1].cast<Intent>()?.run {
      DebugUtil.log("[e_PeekIntent]${flattenBundle(extras)}")
    }
  }

  val y_b_a = hookAfter {
    log("y_b_a", "arg0:${args[0]} | arg2:${args[2]} | result:${result}")
    log("y_b_a", "packageName:${args[1].getField<String>("packageName")}")
    thisObject.getField<Any>("g")?.run { log("y_b_a", "$this") }
  }

  val PackageManagerCompat_a = hookBefore {
    log("PackageManagerCompat_a", "${args[0]} |  | ${args[2]}")
    "${args[0]}".basename()?.also { apk ->
      Cache.currentInstallPackage = apk
      log("PackageManagerCompat_a", "decode:${URLDecoder.decode(apk, "utf-8")}")
    }
  }

  val disableInstallPackage = hookAfter {
    Cache.currentInstallPackage?.run {
      for (pkg in Const.disablePackageList) {
        if (this.startsWith(pkg)) {
          "${URLDecoder.decode(pkg, "utf-8")} is not allowed!".toast(MainHook.marketHookContext.ref)
          result = null
          break
        }
      }
    }
    Cache.currentInstallPackage = null
  }

  val protectionPackage = listOf(
      "com.topjohnwu.magisk",
      "org.meowcat.edxposed.manager",
      "com.example.xposedtest",
      "com.example.miinfo" // Test purpose
  )

  val peekDeletingPackage = hookBefore {
    thisObject
        .getField<Any>("c")
        ?.getField<Any>("L")
        .cast<ArrayList<String>>()
        ?.apply {
          log("UninstallAppsFragment", this.joinToString())
          for (pkg in protectionPackage)
            remove(pkg)
        }
  }

  val preventPackage = listOf(
      "com.ss.android.ugc.aweme",
      "com.smile.gifmaker"
  )
  var doPrevent = false

  var startInstall = hookBefore {
    doPrevent = false
    // gLog("startInstall", it.args[0])
    args[0].getField<String>("packageName")
        ?.run { doPrevent = preventPackage.contains(this) }
    // gLog("startInstall", doPrevent)
  }

  val preventInstall = hookBefore {
    // gLog("preventInstall", doPrevent)
    // gLog("preventInstall", it.args.map { "$it" })
    if (doPrevent)
      args[0].setField("c", Uri.EMPTY)
  }
}