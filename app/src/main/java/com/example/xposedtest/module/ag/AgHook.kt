package com.example.xposedtest.module.ag

import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

class AgHook(param: XC_LoadPackage.LoadPackageParam)
  : HookEntry(param, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookAd() {
    "com.linkfungame.ag.advert.AdvertOperation".`class`()!!
        .apply {
          hook("searchAdvert", C.String,
              hookAfter {
                log("SearchAdvert", *args)
                /* Prevent ads (not Ad01) would cause crash */
                if ("${args[0]}".equals("AD01"))
                  result = null
              })
        }
  }
}