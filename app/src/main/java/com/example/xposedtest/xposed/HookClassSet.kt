package com.example.xposedtest.xposed

import com.example.xposedtest.module.ag.AgHook
import com.example.xposedtest.module.douyin.DouYinHook
import com.example.xposedtest.module.kuai.KuaiHook
import com.example.xposedtest.module.miui.*
import com.example.xposedtest.module.tomato.TomatoHook
import com.example.xposedtest.module.wx.WxHook

object HookClassSet {

  val data = listOf(
      SecurityCenterHook::class.java,
      WxHook::class.java,
      SettingsHook::class.java,
      DouYinHook::class.java,
      TomatoHook::class.java,
      KuaiHook::class.java,
      AgHook::class.java,
      MiuiHomeHook::class.java,
      MiuiMarketHook::class.java,
      PackageInstallerHook::class.java,
      UpdaterHook::class.java
  )
}