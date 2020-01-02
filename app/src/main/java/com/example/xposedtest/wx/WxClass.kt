package com.example.xposedtest.wx

import com.example.xposedtest.xposed.ClassLoaderType
import com.example.xposedtest.xposed.XposedUtil
import com.example.xposedtest.xposed.`class`

class PackageNode(val parent: PackageNode?, val name: String) {

  private val children = mutableMapOf<String, PackageNode>()

  override fun toString(): String {
    if (parent == null)
      return name
    return "$parent.$name"
  }

  operator fun get(k: String): PackageNode {
    return children[k]?: PackageNode(null, "")
  }

  operator fun set(k: String, node: PackageNode) {
    children[k] = node
  }

  fun Get(k: String): PackageNode? {
    return children[k]
  }

  fun Set(k: String) {
    children[k] = PackageNode(this, k)
  }

}

object mm {

  private val tencent_ = PackageNode(null, "com.tencent")
  private val mm_ = PackageNode(tencent_, "mm")

  object tinker {
    private val tinker_ = PackageNode(tencent_, "tinker")
    val TinkerApplication: String by lazy { PackageNode(tinker_, "loader.app.TinkerApplication").toString() }
    val TinkerInstaller: String by lazy { PackageNode(tinker_, "lib.e.c").toString() }
  }

  object kernel {
    private val kernel_ = PackageNode(mm_, "kernel")
    val g by lazy { PackageNode(kernel_, "g").toString() }
    val a by lazy { PackageNode(kernel_, "a").toString() }

    object c {
      private val c_ = PackageNode(kernel_, "c")
      val a: String by lazy { PackageNode(c_, "a").toString() }
      val c: String by lazy { PackageNode(c_, "c").toString() }
    }
  }

  object model {
    private val model_ = PackageNode(mm_, "model")
    val ay by lazy { PackageNode(model_, "ay").toString() }
  }

  object storage {
    private val storage_ = PackageNode(mm_, "storage")
    // userinfo & userinfo2
    val z by lazy { PackageNode(storage_, "z").toString() }
  }

  object wcdb {
    private val wcdb_ = PackageNode(tencent_, "wcdb")
    val SQLiteDatabase: String by lazy { PackageNode(wcdb_, "database.SQLiteDatabase").toString() }
  }

  object am {
    private val am_ = PackageNode(mm_, "am")
    val a: String by lazy { PackageNode(am_, "a").toString() }
    val b: String by lazy { PackageNode(am_, "b").toString() }
  }

  object compatible {
    private val compatible_ = PackageNode(mm_, "compatible")

    object util {
      private val util_ = PackageNode(compatible_, "util")
      val k: String by lazy { PackageNode(util_, "k").toString() }
    }
  }

}

object WxClass {
  val WebViewUI by lazy { XposedUtil.findClass("com.tencent.mm.plugin.webview.ui.tools.WebViewUI") }

  val G by lazy { XposedUtil.findClass("com.tencent.mm.kernel.g") }

  val MODEL_C by lazy { XposedUtil.findClass("com.tencent.mm.model.c") }
  val MODEL_M by lazy { XposedUtil.findClass("com.tencent.mm.model.m") }

  val PROTOBUF_BRJ by lazy { XposedUtil.findClass("com.tencent.mm.protocal.protobuf.brj") }
  val PROTOBUF_BAC by lazy { XposedUtil.findClass("com.tencent.mm.protocal.protobuf.bac") }
  val PROTOBUF_AZZ by lazy { XposedUtil.findClass("com.tencent.mm.protocal.protobuf.azz") }

  val A_A_J_A by lazy { XposedUtil.findClass("com.tencent.mm.plugin.messenger.foundation.a.a.j.a") }

  val TabsAdapter by lazy { XposedUtil.findClass("com.tencent.mm.ui.MainTabUI.TabsAdapter") }
  val BR_D by lazy { XposedUtil.findClass("com.tencent.mm.br.d") }

  val JSAPI_I_A by lazy { XposedUtil.findClass("com.tencent.mm.plugin.webview.ui.tools.jsapi.i.a") }

  val PLUGINSDK_MODEL_M by lazy { XposedUtil.findClass("com.tencent.mm.pluginsdk.model.m") }

  val SQLiteDatabase by lazy { XposedUtil.findClass("com.tencent.wcdb.database.SQLiteDatabase") }
  val SQLiteCipherSpec by lazy { XposedUtil.findClass("com.tencent.wcdb.database.SQLiteCipherSpec") }
  val CursorFactory by lazy { XposedUtil.findClass("com.tencent.wcdb.database.SQLiteDatabase.CursorFactory") }
  val DatabaseErrorHandler by lazy { XposedUtil.findClass("com.tencent.wcdb.DatabaseErrorHandler") }

  val Preference by lazy { XposedUtil.findClass("com.tencent.mm.ui.base.preference.Preference") }
  val PREFERENCE_F_IF by lazy { XposedUtil.findClass("com.tencent.mm.ui.base.preference.f") }
  val chatroomInfoUI by lazy { XposedUtil.findClass("com.tencent.mm.chatroom.ui.ChatroomInfoUI") }

  val STUB_C_A_A by lazy { XposedUtil.findClass("com.tencent.mm.plugin.webview.stub.c.a.a") }
  val STUB_D_A_A by lazy { XposedUtil.findClass("com.tencent.mm.plugin.webview.stub.d.a.a") }
  val STUB_E_A_A by lazy { XposedUtil.findClass("com.tencent.mm.plugin.webview.stub.e.a.a") }
  val STUB_D_A by lazy { XposedUtil.findClass("com.tencent.mm.plugin.webview.stub.d.a") }

  val WebView by lazy { XposedUtil.findClass("com.tencent.xweb.WebView") }
  val PreloadLogic by lazy { XposedUtil.findClass("com.tencent.mm.plugin.brandservice.ui.timeline.preload.PreloadLogic") }
  val BRANDSERVICE_A_B_A by lazy { XposedUtil.findClass("com.tencent.mm.plugin.brandservice.a.b.a") }

  val APPBRAND_S_D by lazy { XposedUtil.findClass("com.tencent.mm.plugin.appbrand.s.d") }

  val cf_a by lazy { "com.tencent.mm.cf.a".`class`(ClassLoaderType.Wechat) }
  val a_g by lazy { "com.tencent.mm.a.g".`class`(ClassLoaderType.Wechat) }
}