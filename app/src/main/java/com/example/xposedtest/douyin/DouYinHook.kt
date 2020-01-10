package com.example.xposedtest.douyin

import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class DouYinHook(param: XC_LoadPackage.LoadPackageParam) : HookEntry(param, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookVideo() {
    "kotlin.jvm.internal.Intrinsics".`class`()!!
        .apply {
          hook("checkExpressionValueIsNotNull",
              C.Object, C.String,
              hookBefore {
                if ("${args[1]}".contains(Regex("uri")))
                  log("FakeLog", *args)
              })
        }

    // "com.ss.android.ugc.aweme.feed.model.Video".`class`()!!
    //     .apply {
    //       hook("isHasWaterMark", hookAfter {
    //         /**
    //          * true, normal video
    //          * false, big video
    //          */
    //         log("WaterMark", result)
    //       })
    //     }

    "com.squareup.wire.ProtoReader".`class`()!!
        .apply {
          hook("readString",
              hookAfter {
                "$result".let { s ->
                  if (s.contains("play") || s.startsWith("TikTok"))
                    log("(String)ProtoReader", result)
                }
              })
        }

    C.JSONObject.apply {
      hookCtor(C.String,
          hookBefore {
            val s = "${args[0]}"
            if (s.contains(UrlHelper.Tag.VIDEO)) {
              log("Group", "@@@@@@START")
              UrlHelper.pattern.matcher(s).apply {
                while (find()) {
                  // log("Group", s.substring(start(), end()))
                  group().apply {
                    log("Raw", this)
                    log("PlayUrl", substringBefore("&line=") + UrlHelper.suffix)
                  }
                }
              }
              log("Group", "@@@@@@END")
            }
          })
    }
  }
}