package com.example.xposedtest.module.miui

import android.widget.Button
import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.*
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HookClass("com.miui.securitycenter")
class SecurityCenterHook(lpparam: XC_LoadPackage.LoadPackageParam) : HookEntry(lpparam, HookContext()), IHookEntry {

  private val delayPool = listOf<Long>(200, 400, 600, 800, 1000)

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookUninstall() {
    var cachedPackageName: String? = null

    "com.miui.appmanager.ApplicationsDetailsActivity".`class`()!!
        .apply {
          hook("aQe", C.String, C.Int,
              hookBefore {
                log("Uninstall", *args)
                //log("Uninstall@aCf", thisObject.getField("aCf"))
                if (Config.Package.isProtectApp(args[0]))
                  args[0] = null
              })

          hook("aPD", hookBoth({
            cachedPackageName = thisObject.getField("mPackageName")
            if (Config.Package.isProtectApp(cachedPackageName)) {
              thisObject.setField("mPackageName", "com.jeejen.family.miui")
            } else {
              cachedPackageName = null
            }
          }, {
            if (cachedPackageName != null) {
              thisObject.setField("mPackageName", cachedPackageName)
              cachedPackageName = null
            }
          }))
        }
  }

  @HookMethod
  private fun hook() {
    "${SecurityCenter.RemoteProvider}".`class`()!!.hook("hD",
        C.String, C.Boolean,
        hookBefore {
          val k = args[0].cast<String>() ?: return@hookBefore
          if (k.equals("security_adb_install_enable"))
            args[1] = false
          log("BlockPermissionCheck", "enable adb install")
        })

    "${SecurityCenter.AdbInstallActivity}".`class`()!!
        .apply {
          hook("Kt",
              C.View,
              hookAfter {
                // In kotlin, it is really hard to represent `Class[]`
                // val clsArr: Array<Class<*>> = Array(1, {IntArray::class.java.componentType})
                // val cls = Class.forName("com.android.internal.app.AlertController")
                val okButton: Button = thisObject
                    .getField<Any>("BT")
                    ?.callMethod("getButton", -2)
                    ?.cast<Button>() ?: return@hookAfter
                MainScope().launch {
                  delay(delayPool.shuffled().last())
                  log("AutoClick", okButton)
                  okButton.performClick()
                }
              })

          hook("onClick", C.DialogInterface, C.Int,
              hookBefore {
                log("onClick", *args)
                args[1] = -2
              })
        }
  }

}