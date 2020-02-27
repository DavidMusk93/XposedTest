package com.example.xposedtest.module.test

import android.graphics.Color
import android.widget.TextView
import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.getField
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

@HookClass("com.example.xposedtest")
class MainAppHook(param: XC_LoadPackage.LoadPackageParam)
  : HookEntry(param, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookTextView() {
    "$packageName.MainActivity".`class`()!!
        .apply {
          hook("onCreate", C.Bundle,
              hookAfter {
                thisObject.getField<TextView>("tv")?.apply {
                  text = "X P O S E D\nI S\nR E A D Y !"
                  setTextColor(Color.RED)
                }
              })
        }
  }
}