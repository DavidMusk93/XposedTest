package com.example.xposedtest.module.miui

import android.content.Intent
import android.view.MenuItem
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

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookUninstall() {
    var packageName: String? = null
    "com.miui.appmanager.ApplicationsDetailsActivity".`class`()!!.apply {
      hook("onCreate", C.Bundle, hookBefore {
        val intent = thisObject.callMethod("getIntent").cast<Intent>()
        packageName = intent?.getStringExtra("package_name")
      })

      val nameOfSecondMenuItem: String = this.fieldName("MenuItem", 2) ?: return
      hook("onCreateOptionsMenu", C.Menu, hookAfter {
        if (Config.Package.isProtectApp(packageName)) {
          thisObject.getField<MenuItem>(nameOfSecondMenuItem)?.apply { isVisible = false }
        }
      })
    }
  }

  @HookMethod
  private fun hook() {
    kotlin.runCatching {
      "${SecurityCenter.RemoteProvider}".`class`()!!.hook("hD",
          C.String, C.Boolean,
          hookBefore {
            val k = args[0].cast<String>() ?: return@hookBefore
            if (k.equals("security_adb_install_enable"))
              args[1] = false
            log("BlockPermissionCheck", "enable adb install")
          })
    }.onFailure { log("Failure@AdbInstall", it) }

    "${SecurityCenter.AdbInstallActivity}".`class`()!!.apply {
      val delayPool = listOf<Long>(200, 400, 600, 800, 1000)
      val nameOfAlert: String = this.fieldName("Object") ?: return
      hook("onCreate", C.Bundle, hookAfter {
        val okButton: Button = thisObject
            .getField<Any>(nameOfAlert)
            ?.callMethod("getButton", -2)
            ?.cast<Button>() ?: return@hookAfter
        MainScope().launch {
          delay(delayPool.shuffled().last())
          log("AutoClick", okButton)
          okButton.performClick()
        }
      })

      hook("onClick", C.DialogInterface, C.Int, hookBefore {
        log("onClick", *args)
        args[1] = -2
      })
    }
  }

}