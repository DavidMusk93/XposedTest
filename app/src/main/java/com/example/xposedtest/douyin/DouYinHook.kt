package com.example.xposedtest.douyin

import com.example.xposedtest.xposed.HookContext
import com.example.xposedtest.xposed.HookEntry
import com.example.xposedtest.xposed.IHookEntry
import de.robv.android.xposed.callbacks.XC_LoadPackage

class DouYinHook(param: XC_LoadPackage.LoadPackageParam): HookEntry(param, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(javaClass.simpleName)
    hook()
  }

  private fun hook() {

  }
}