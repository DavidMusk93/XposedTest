package com.example.xposedtest.xposed

import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.module.ag.AgHook
import com.example.xposedtest.module.douyin.DouYinHook
import com.example.xposedtest.module.kuai.KuaiHook
import com.example.xposedtest.module.miui.*
import com.example.xposedtest.module.test.MainAppHook
import com.example.xposedtest.module.tomato.TomatoHook
import com.example.xposedtest.module.wx.WxHook

class HookSet : Iterator<Class<*>> {

  private val data = listOf(
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
      UpdaterHook::class.java,
      MainAppHook::class.java
  )

  private var i = 0

  override fun hasNext(): Boolean {
    return i < data.size
  }

  override fun next(): Class<*> {
    return data[i++]
  }
}

object HookMap {

  private val map = mutableMapOf<Int, Class<*>>().apply {
    HookSet().forEach {
      it.getAnnotation(HookClass::class.java)?.let { clz ->
        this[clz.pkg.hashCode()] = it
      }
    }
  }

  operator fun get(hash: Int) = if (map.containsKey(hash)) map[hash] else null
}