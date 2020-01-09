package com.example.xposedtest.douyin

import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class DouYinHook(param: XC_LoadPackage.LoadPackageParam) : HookEntry(param, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    for (method in this::class.java.declaredMethods) {
      method.getAnnotation(HookMethod::class.java) ?: continue
      method.invoke(this)
    }
  }

  @HookMethod
  private fun hookVideoDownload() {
    "kotlin.jvm.internal.Intrinsics".`class`()!!
        .apply {
          hook("checkExpressionValueIsNotNull",
              C.Object, C.String,
              hookBefore {
                val k = args[1].cast<String>() ?: return@hookBefore
                if (k.contains(Regex("url|uri")))
                  log("checkExpressionValueIsNotNull", *args)
              })
        }

    "com.ss.android.ugc.aweme.feed.model.Video".`class`()!!
        .apply {
          hook("isHasWaterMark", hookAfter {
            /**
             * true, normal video
             * false, big video
             */
            log("WaterMark", result)
          })
        }
  }
}