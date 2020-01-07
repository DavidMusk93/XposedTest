package com.example.xposedtest.xposed

import android.app.Activity
import android.content.ContentValues
import com.example.xposedtest.extension.amrToMp3
import com.example.xposedtest.extension.peekMessage
import com.example.xposedtest.hash.WxTable
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.wx.mm
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.ref.WeakReference

class WxHook(lpparam: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lpparam, WxHookContext())
    , IHookEntry {

  object Ref {
    var LauncherUI: WeakReference<Activity>? = null
    var BrandServiceIndexUI: WeakReference<Activity>? = null
    var ChattingUI: WeakReference<Activity>? = null
    var ContactInfoUI: WeakReference<Activity>? = null
    var WebviewMpUI: WeakReference<Activity>? = null
    var TmplWebViewTooLMpUI: WeakReference<Activity>? = null
  }

  object Ht {
    val LauncherUI = "com.tencent.mm.ui.LauncherUI".hashCode()
    val BrandServiceIndexUI = "com.tencent.mm.plugin.brandservice.ui.BrandServiceIndexUI".hashCode()
    val ChattingUI = "com.tencent.mm.ui.chatting.ChattingUI".hashCode()
    val ContactInfoUI = "com.tencent.mm.plugin.profile.ui.ContactInfoUI".hashCode()
    val WebviewMpUI = "com.tencent.mm.plugin.webview.ui.tools.WebviewMpUI".hashCode()
    val TmplWebViewTooLMpUI = "com.tencent.mm.plugin.webview.ui.tools.preload.TmplWebViewTooLMpUI".hashCode()
  }

  private var loadFlag = false

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private val g: Class<*> by lazy { mm.kernel.g.`class`()!! }

  private fun hook() {
    // g.hook("b",
    //     Class::class.java, mm.kernel.c.a.`class`(), // thank TypeErase
    //     hookBefore { gLog("@InjectClassToKernel", *args) })

    // g.hook("a",
    //     Class::class.java, mm.kernel.c.c.`class`(), // thank TypeErase
    //     hookBefore { gLog("@InjectClassToKernel@2", *args) })

    // // Generate database directory (echo -n "mm$uin" | md5sum)
    // context.findClass(MM.Kernel.a)!!.hook("cU", C.Boolean,
    //     hookBefore {
    //       gLog("@cU", it.thisObject.getField("fwN"))
    //     })

    // /** So many Beats! */
    // val AppMethodBeat = context.findClass("com.tencent.matrix.trace.core.AppMethodBeat")
    // // var preIndex = -1
    // AppMethodBeat!!.hook("i", C.Int, hookBefore {
    //   // val i: Int = it.args[0].cast()!!
    //   // if (preIndex == -1) {
    //   //   preIndex = i // Cache index
    //   // } else if (i == preIndex) {
    //   //   gLog("@AppMethodBeat", preIndex)
    //   //   preIndex = -1 // Reset index
    //   // }
    //   gLog("@AppMethodBeat", it.args[0])
    // })

    // // Is Tinker enabled
    // mm.tinker.TinkerApplication.`class`()!!.hookCtor(
    //     C.Int, C.String, C.String, C.Boolean,
    //     hookBefore {
    //       gLog("@TinkerApplication", *args)
    //     })

    // Prevent Tinker upgrade patch
    mm.tinker.TinkerInstaller.`class`()!!.hook("cn",
        C.Context, C.String,
        hookBefore {
          gLog("@onReceiveUpgradePatch", args[1])
          args[1] = null
        })

    mm.wcdb.SQLiteDatabase.`class`()!!.hook(
        "insertWithOnConflict",
        C.String, C.String, C.ContentValues, C.Int,
        hookBefore {
          val table: String = args[0].cast() ?: return@hookBefore
          when (table.hashCode()) {
            WxTable.message ->
              args[2].cast<ContentValues>()?.peekMessage()
            WxTable.BizTimeLineInfo ->
              return@hookBefore
            WxTable.WxFileIndex2 ->
              args[2].cast<ContentValues>()?.amrToMp3()
            else ->
              gLog("@insertWithOnConflict", *args)
          }
        })

    // mm.am.b.`class`()!!.hook("dA",
    //     C.Boolean,
    //     hookBefore{
    //       val list = thisObject.getField<LinkedList<String>>("gsj")?: return@hookBefore
    //       val taskMap = thisObject.getField<Map<String, Any>>("gsk")?: return@hookBefore
    //       taskMap[list.peek()]?.apply {
    //         gLog("@TaskType", this)
    //         gLog("@CdnTransportService",
    //             getField("dsz"), // false for receive, true for send
    //             getField("field_fileType"),
    //             "eNN|"+getField("eNN"),
    //             "eNQ|"+getField("eNQ"),
    //             "eNS|"+getField("eNS"),
    //             "eNT|"+getField("eNT"))
    //       }
    //     })

    mm.am.a.`class`()!!.hook("onDownloadProgressChanged",
        C.String, C.Long, C.Long, C.Boolean,
        hookAfter { gLog("@onDownloadProgressChanged", *args) })

    mm.compatible.util.k.`class`()!!.hook("b",
        C.String, C.ClassLoader,
        hookBefore { log("LoadLibrary", args[0]) })
  }

}