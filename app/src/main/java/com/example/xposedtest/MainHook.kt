package com.example.xposedtest

import android.app.Activity
import android.app.Application
import com.example.xposedtest.douyin.DouYinHook
import com.example.xposedtest.kuai.KuaiHook
import com.example.xposedtest.miui.MiuiMarketClass
import com.example.xposedtest.miui.MiuiMarketHook
import com.example.xposedtest.miui.SecurityCenterHook
import com.example.xposedtest.miui.SettingsHook
import com.example.xposedtest.shyd.ShydHook
import com.example.xposedtest.tomato.TomatoHook
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.DebugUtil
import com.example.xposedtest.utility.is_N
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.Job
import java.lang.ref.WeakReference

class MainHook : IXposedHookLoadPackage {
  companion object {
    var mainDatabase: Any? = null

    var scrollJob: Job? = null
    var backJob: Job? = null

    var gLaunchUIRef: WeakReference<Activity>? = null
    var gBrandServiceIndexUIRef: WeakReference<Activity>? = null
    var gChattingUIRef: WeakReference<Activity>? = null
    var gContactInfoUIRef: WeakReference<Activity>? = null
    var gWebviewMpUIRef: WeakReference<Activity>? = null
    var gTmplWebViewTooLMpUI: WeakReference<Activity>? = null

    var gPassTicket: String? = null
    var gJsApiProxy: Any? = null

    var gTestUrl: String? = null
    var gTest = false

    var gMiuiSettingsRef: WeakReference<Application>? = null
    var gMiuiSettingsClassLoader: ClassLoader? = null

    val wechatHookContext = WxHookContext()
    val marketHookContext = HookContext()
  }

  override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
    when (lpparam.packageName.hashCode()) {
      // Ht.Package.WeChat -> {
      //   fun xLog(msg: String?) = DebugUtil.log("[handleLoadPackage]$msg")
      //   with (lpparam) {
      //     xLog("$processName, $isFirstApplication, $appInfo | is_N:${is_N()}")
      //     wechatHookContext.classLoader = classLoader
      //     wechatHookContext.processName = processName
      //   }

      //   val CS = WxClass
      //   val MS = WxMethodHook

      //   wechatHookContext.baseHook("WechatHook")

      //   /** database concerned */
      //   // XposedHelpers.findAndHookMethod(WxClass.SQLiteDatabase, "insertWithOnConflict", C.String, C.String, C.ContentValues, C.Int, WxMethodHook.hookDbInsert)
      //   CS.SQLiteDatabase!!.hook("openDatabase", C.String, C.ByteArray, CS.SQLiteCipherSpec, CS.CursorFactory, C.Int, CS.DatabaseErrorHandler, C.Int, MS.openDatabase)

      //   CS.cf_a!!.hook("a", C.String, C.Long, C.Boolean, C.String, MS.cf_a_a)

      //   // XposedHelpers.findAndHookMethod(WxClass.TabsAdapter, "onTabClick", C.Int, WxMethodHook.hookOnTabClick)

      //   /** fetch biz history url */
      //   // XposedHelpers.findAndHookMethod(WxClass.JSAPI_I_A, "aeS", C.String, WxMethodHook.hookBizHistoryUrlUpdate)

      //   /** start activity hook */
      //   // XposedHelpers.findAndHookMethod(WxClass.BR_D, "b", C.Context, C.String, C.String, C.Intent, WxMethodHook.hookStartActivityB4)
      //   // XposedHelpers.findAndHookMethod(WxClass.BR_D, "b", C.Context, C.String, C.String, C.Intent, C.Int, WxMethodHook.hookStartActivityB5)
      //   // XposedHelpers.findAndHookMethod(WxClass.PreloadLogic, "a", C.Context, C.String, *Array(3, {C.Int}), C.Intent, C.Int, WxClass.BRANDSERVICE_A_B_A, C.Int, WxMethodHook.hookStartActivityA9)

      //   /** auto scroll xwalkview */
      //   // XposedHelpers.findAndHookMethod(WxClass.WebViewUI, "onPause", WxMethodHook.hookWxWebViewUIOnPause)
      //   // XposedHelpers.findAndHookMethod(WxClass.WebViewUI, "onResume", WxMethodHook.hookWxWebViewUIOnResume)

      //   /** url transform (remote rpc) */
      //   // XposedHelpers.findAndHookMethod(WxClass.STUB_D_A_A, "acE", C.String, WxMethodHook.hookShortenUrl)

      //   /** read_num & like_num; elected_comment */
      //   // XposedHelpers.findAndHookMethod(WxClass.STUB_E_A_A, "a", C.String, C.String, C.Bundle, C.Boolean, WxMethodHook.hookBizComment)
      // }

      Ht.Package.WeChat ->
        WxHook(lpparam).setupHook()

      Ht.Package.Zuiyou ->
        ZuiyouHook(lpparam).setupHook()

      Ht.Package.MiuiSettings ->
        SettingsHook(lpparam).setupHook()

      Ht.Package.SecurityCenter ->
        SecurityCenterHook(lpparam).setupHook()

      Ht.Package.DouYin ->
        DouYinHook(lpparam).setupHook()

      Ht.Package.Tomato ->
        TomatoHook(lpparam).setupHook()

      Ht.Package.Shyd ->
        ShydHook(lpparam).setupHook()

      Ht.Package.Kuai ->
        KuaiHook(lpparam).setupHook()

      Ht.Package.MiuiMarket -> {
        val CS = MiuiMarketClass
        val MS = MiuiMarketHook

        fun xLog(msg: String?) = DebugUtil.log("[MiuiMarket]$msg")

        with(lpparam) {
          marketHookContext.classLoader = classLoader
          xLog("$processName | $isFirstApplication | $appInfo")
        }

        marketHookContext.baseHook("MiuiMarketHook")

        // XposedHelpers.findAndHookMethod(MiuiMarketClass.MarketPreference, "onPreferenceClick", C.Preference, MiuiMarketHook.onPreferenceClick)
        // XposedHelpers.findAndHookMethod(MiuiMarketClass.MarketPreference, "onPreferenceChange", C.Preference, C.Object, MiuiMarketHook.onPreferenceChange)

        // XposedHelpers.findAndHookMethod(MiuiMarketClass.fb, "a", C.String, C.Boolean, MiuiMarketHook.preferenceForceFalse)
        XposedHelpers.findAndHookMethod(MiuiMarketClass.MarketPreference, "i", MiuiMarketHook.filterPreferenceList)

        // XposedHelpers.findAndHookMethod(MiuiMarketClass.Ha, "q", MiuiMarketHook.forceReturnTrue)
        XposedHelpers.findAndHookMethod(MiuiMarketClass.AboutPreferenceActivity, "onCreate", C.Bundle, MiuiMarketHook.disableAutoUpdateMarket)

        // JavaBridge (Js & Java interoperability?)
        // XposedHelpers.findAndHookMethod(MiuiMarketClass.e, "a", MiuiMarketClass.Ibd, C.Intent, C.Int, C.Bundle, MiuiMarketHook.e_PeekIntent)

        // Download thread
        XposedHelpers.findAndHookMethod(MiuiMarketClass.y_b, "a", C.String, MiuiMarketClass.DownloadInstallInfo, C.String, MiuiMarketHook.y_b_a)

        /** Hook deleting packages */
        CS.lh.hook("onClick", C.DialogInterface, C.Int, MS.peekDeletingPackage)

        /** Hook installing packages */
        if (is_N()) {
          XposedHelpers.findAndHookMethod(MiuiMarketClass.l, "a", C.Uri, MiuiMarketClass.IPackageInstallObserver, C.String, MiuiMarketHook.PackageManagerCompat_a)
          XposedHelpers.findAndHookMethod(MiuiMarketClass.Za, "b", Class::class.java, C.String, C.String, MiuiMarketHook.disableInstallPackage)
        } else {
          /* Download ProgressBar */
          // XposedHelpers.findAndHookMethod(MiuiMarketClass.downloadinstall_G, "a", C.String, C.Int, C.Int, C.Long, C.Long, MiuiMarketHook.dumpArgument)

          /* Download & Install stage */
          // CS.TaskManager.hook("a", CS.DownloadInstallInfo, CS.TaskStep, MS.taskManagerDispatcher)

          /* This is a critical function! (hook check?) */
          // CS.downloadinstall_A!!.hook("a", C.Int, CommonHook.dumpRes)

          CS.downloadinstall_l_b!!.hook("a", CS.DownloadInstallInfo, MS.startInstall)
          CS.downloadinstall_M!!.hook("a", CS.downloadinstall_q, C.PackageInstaller, MS.preventInstall)
        }
      }
    }
  }
}
