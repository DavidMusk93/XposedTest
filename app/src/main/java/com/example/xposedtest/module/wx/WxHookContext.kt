package com.example.xposedtest.module.wx

import android.app.Activity
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.xposed.HookContext
import com.example.xposedtest.xposed.gLog
import com.example.xposedtest.xposed.hook
import com.example.xposedtest.xposed.hookAfter
import java.lang.ref.WeakReference

// class WxHookContext
//   : HookContext() {
//
//   override fun onResume(tag: String) {
//     C.Activity.hook("onResume", hookAfter {
//       gLog(tag + "_onResume", thisObject)
//       thisObject.cast<Activity>()?.run {
//         val className = "$this".substringBeforeLast('@')
//         when (className.hashCode()) {
//           WxUiHt.LauncherUI -> {
//             MainHook.gLaunchUIRef = WeakReference(this)
//           }
//           WxUiHt.BrandServiceIndexUI -> {
//             MainHook.gBrandServiceIndexUIRef = WeakReference(this)
//           }
//           WxUiHt.ChattingUI -> {
//             MainHook.gChattingUIRef = WeakReference(this)
//           }
//           WxUiHt.ContactInfoUI -> {
//             MainHook.gContactInfoUIRef = WeakReference(this)
//           }
//           WxUiHt.WebviewMpUI -> {
//             MainHook.gWebviewMpUIRef = WeakReference(this)
//           }
//           WxUiHt.TmplWebViewTooLMpUI -> {
//             MainHook.gTmplWebViewTooLMpUI = WeakReference(this)
//           }
//           else -> {
//             gLog(tag, "there is no Ref to cache $className")
//           }
//         }
//       }
//     })
//   }
// }

class WxHookContext : HookContext() {

  override fun onResume(tag: String) {
    C.Activity.hook(
        "onResume",
        hookAfter {
          gLog(tag + "_onResume", thisObject)
          thisObject.cast<Activity>()?.run {
            val className = "$this".substringBeforeLast('@')
            when (className.hashCode()) {
              WxHook.Ht.LauncherUI ->
                WxHook.Ref.LauncherUI = WeakReference(this)
              WxHook.Ht.BrandServiceIndexUI ->
                WxHook.Ref.BrandServiceIndexUI = WeakReference(this)
              WxHook.Ht.ChattingUI ->
                WxHook.Ref.ChattingUI = WeakReference(this)
              WxHook.Ht.ContactInfoUI ->
                WxHook.Ref.ContactInfoUI = WeakReference(this)
              WxHook.Ht.WebviewMpUI ->
                WxHook.Ref.WebviewMpUI = WeakReference(this)
              WxHook.Ht.TmplWebViewTooLMpUI ->
                WxHook.Ref.TmplWebViewTooLMpUI = WeakReference(this)
              else ->
                gLog(tag + "_onResume", "There is no Ref to cache $className")
            }
          }
        })
  }

}
