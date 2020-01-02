package com.example.xposedtest.wx

import com.example.xposedtest.xposed.XposedUtil
import de.robv.android.xposed.XposedHelpers
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class Delegator {
  class VH : ReadOnlyProperty<Any?, Any?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Any? {
      val plugin_messenger_foundation_a_j_cls = XposedUtil.findClass("com.tencent.mm.plugin.messenger.foundation.a.j")
      return XposedHelpers.callStaticMethod(WxClass.G, "L", plugin_messenger_foundation_a_j_cls)?.run {
        XposedHelpers.callMethod(this, "VH")
      }
    }
  }

  class EVJ : ReadOnlyProperty<Any?, Any?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): Any? {
      return XposedHelpers.callStaticMethod(WxClass.G, "Pw")
    }
  }
}