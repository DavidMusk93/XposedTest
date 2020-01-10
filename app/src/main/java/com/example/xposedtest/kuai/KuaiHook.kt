package com.example.xposedtest.kuai

import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class KuaiHook(param: XC_LoadPackage.LoadPackageParam) : HookEntry(param, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookTrivial() {
    C.Uri.hook("parse", C.String,
        hookBefore {
          if ("${args[0]}".contains(UrlHelper.Tag.VIDEO))
            log("VideoUri", *args)
        })

    "com.yxcorp.gifshow.model.CDNUrl".`class`()!!
        .apply {
          hook("getUrl",
              hookAfter {
                if ("$result".contains(UrlHelper.Tag.LIVE))
                  log("CDNUrl", result)
              })
        }
  }
}