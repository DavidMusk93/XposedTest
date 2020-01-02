package com.example.xposedtest

object Ht {

  object Package {
    val WeChat: Int by lazy { "com.tencent.mm".hashCode() }
    val MiuiSettings: Int by lazy { "com.android.settings".hashCode() }
    val MiuiMarket: Int by lazy { "com.xiaomi.market".hashCode() }
    val Zuiyou: Int by lazy { "cn.xiaochuankeji.tieba".hashCode() }
    val SecurityCenter: Int by lazy { "com.miui.securitycenter".hashCode() }
  }
}