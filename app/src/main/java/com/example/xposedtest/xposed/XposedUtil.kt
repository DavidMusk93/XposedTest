package com.example.xposedtest.xposed

import com.example.xposedtest.MainHook
import de.robv.android.xposed.XposedHelpers

enum class ClassLoaderType {
  Wechat,
  MiuiSettings,
  MiuiMarket
}

fun String.`class`(type: ClassLoaderType = ClassLoaderType.MiuiMarket): Class<*>? {
  return when (type) {
    ClassLoaderType.Wechat ->
      XposedHelpers.findClassIfExists(this, MainHook.wechatHookContext.classLoader)
    ClassLoaderType.MiuiMarket ->
      XposedHelpers.findClassIfExists(this, MainHook.marketHookContext.classLoader)
    ClassLoaderType.MiuiSettings ->
      XposedHelpers.findClassIfExists(this, MainHook.gMiuiSettingsClassLoader)
    else ->
      null
  }
}

object XposedUtil {
  fun findClass(name: String): Class<*>? {
    return XposedHelpers.findClassIfExists(name, MainHook.wechatHookContext.classLoader)
  }
}