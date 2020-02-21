package com.example.xposedtest.module.miui

import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.callMethod
import com.example.xposedtest.utility.getField
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.Exception

class MiuiHomeHook(lpparam: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lpparam, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookUnintall() {
    val ShortcutInfo = "$packageName.launcher.ShortcutInfo".`class`()
    "$packageName.launcher.UninstallDialog".`class`()!!
        .apply {
          hook("deletePackage", C.String, ShortcutInfo,
              hookBefore {
                log("Uninstall", *args)
                if (Config.Package.protectionAppList.contains("${args[0]}")) {
                  args[0] = null
                  args[1] = null
                }
              })
        }

    val DragObject="$packageName.launcher.DragObject".`class`()
    val ItemInfo="$packageName.launcher.ItemInfo".`class`()
    val CellInfo="$packageName.launcher.CellLayout.CellInfo".`class`()

    "$packageName.launcher.DeleteZone".`class`()!!
        .apply {
          //hook("deleteItem",DragObject,ItemInfo,
          //    hookBefore {
          //      log("DeleteZone",*args)
          //      log("ItemType",args[1].getField("itemType"))
          //    })

          //hook("onDrop",DragObject,
          //    hookBefore {
          //      log("CanBeDeleted",thisObject.callMethod("checkDeletedOperationPermission",args[0]))
          //      log("DragInfo",args[0].callMethod("getDragInfo"))
          //    })

          hook("checkDeletedOperationPermission",DragObject,
              hookAfter {
                log("checkDeletedOperationPermission",*args,result)
                //kotlin.runCatching {
                //  throw Exception("trivial")
                //}.onFailure {
                //  it.stackTrace.forEach {
                //    log("StackTrace",it)
                //  }
                //}
                kotlin.runCatching {
                  log("checkDeletedOperationPermission",args[0]
                      .callMethod("getDragInfo")
                      ?.getField<String>("mIconPackage")?.also {
                        if(Config.Package.protectionAppList.contains(it)){
                          result=false
                        }
                      })
                }.onFailure { log("GetField@Failure",it) }
              })
        }

    //"$packageName.launcher.Launcher".`class`()!!
    //    .apply {
    //      hook("onLongClick",C.View, hookBefore { log("onLongClick",*args) })
    //    }
  }
}