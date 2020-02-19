package com.example.xposedtest

import android.app.Activity
import android.app.Application
import com.example.xposedtest.douyin.DouYinHook
import com.example.xposedtest.kuai.KuaiHook
import com.example.xposedtest.miui.SecurityCenterHook
import com.example.xposedtest.miui.SettingsHook
import com.example.xposedtest.module.ag.AgHook
import com.example.xposedtest.module.miui.MiuiHomeHook
import com.example.xposedtest.module.miui.MiuiMarketHook
import com.example.xposedtest.module.miui.PackageInstallerHook
import com.example.xposedtest.shyd.ShydHook
import com.example.xposedtest.tomato.TomatoHook
import com.example.xposedtest.xposed.HookContext
import com.example.xposedtest.xposed.WxHook
import com.example.xposedtest.xposed.WxHookContext
import com.example.xposedtest.xposed.ZuiyouHook
import de.robv.android.xposed.IXposedHookLoadPackage
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

      Ht.Package.Ag ->
        AgHook(lpparam).setupHook()

      Ht.Package.MiuiHome ->
        MiuiHomeHook(lpparam).setupHook()

      Ht.Package.MiuiMarket ->
        MiuiMarketHook(lpparam).setupHook()

      Ht.Package.PackageInstaller ->
        PackageInstallerHook(lpparam).setupHook()
    }
  }
}
