package com.example.xposedtest.shyd

import com.example.xposedtest.utility.C
import com.example.xposedtest.xposed.*
import com.wrbug.dumpdex.Native
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

class ShydHookContext : HookContext() {

  companion object {
    const val PKG = "com.secneo.apkwrapper.ApplicationWrapper"
  }

  override fun attachBaseContext() {
    attach(PKG)
  }

  override fun hook() {
    super.hook()
    hookTrivial()
  }

  private fun hookTrivial() {

  }
}

class ShydHook(param: XC_LoadPackage.LoadPackageParam)
  : HookEntry(param, ShydHookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private fun hook() {
    "com.secneo.apkwrapper.d".`class`()!!.apply {
      hook("b", hookAfter { log("(b)Result", result) })
      hook("onEvent",
          C.Int, C.String,
          hookAfter { log("onEvent", *args) })
    }

    ShydHookContext.PKG.`class`()!!
        .apply {
          hook("attachBaseContext",
              C.Context,
              hookBefore {
                context.processName?.let { name ->
                  if (!name.contains(':')) {
                    log("DexDumper", "Start to dump dex")
                    val path = "/data/data/$name/dump"
                    File(path).apply {
                      if (!exists())
                        mkdirs()
                    }
                    kotlin.runCatching {
                      Native.dump(name)
                    }.onFailure { log("DexDumper", "${it.message}") }
                  }
                }
              })
        }
  }
}