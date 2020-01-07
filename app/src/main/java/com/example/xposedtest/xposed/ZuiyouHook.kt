package com.example.xposedtest.xposed

import com.example.xposedtest.extension.toHexList
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import com.example.xposedtest.utility.getField
import com.example.xposedtest.zuiyou.zuiyou
import de.robv.android.xposed.callbacks.XC_LoadPackage

class ZuiyouHook(lpparam: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lpparam, HookContext()),
    IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private fun hook() {
    "${zuiyou.ui.PageMeFragment}.h".`class`()!!.hook("onClick",
        C.View,
        hookBefore {
          log("me_tab", "onClick")
        })

    zuiyou.defpackage.cz1.`class`()!!.hook("c",
        C.Context, C.String, C.String,
        hookAfter {
          log("LoadLibrary", *args)
          // if ("${args[1]}" == "net_crypto") {
          // }
        })

    /* ExceptionInInitializerError */
    // zuiyou.network.NetCrypto.`class`()!!
    //     .apply {
    //       hook("a", C.String, C.ByteArray, hookAfter { log("a", result) })
    //       hook("b", C.String, C.ByteArray, hookAfter { log("b", result) })
    //     }

    "${zuiyou.defpackage.fy3}.a".`class`()!!.hook("b",
        C.String, hookBefore { log("RequestUrl", *args) })

    zuiyou.defpackage.gy3.`class`()!!.hook("create",
        zuiyou.defpackage.by3.`class`(), C.ByteArray,
        hookBefore {
          args[0].getField<String>("a")?.let { type ->
            args[1].cast<ByteArray>()?.let { data ->
              if (type.startsWith("text/plain"))
                log("TextPlain", String(data))
              else
                log("RequestType", type, data.toHexList())
            }
          }
        })
  }

}