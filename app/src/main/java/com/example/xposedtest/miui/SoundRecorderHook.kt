package com.example.xposedtest.miui

import com.example.xposedtest.annotation.HookClass
import com.example.xposedtest.annotation.HookMethod
import com.example.xposedtest.utility.C
import com.example.xposedtest.xposed.*
import de.robv.android.xposed.callbacks.XC_LoadPackage

@HookClass(pkg = "com.android.soundrecorder")
class SoundRecorderHook(lppram: XC_LoadPackage.LoadPackageParam)
  : HookEntry(lppram, HookContext()), IHookEntry {

  override fun setupHook() {
    super.setupHook(this)
  }

  @HookMethod
  private fun hookRecording() {
    "$packageName.RecorderService".`class`()!!
        .apply {
          hook("startRecording", C.Context, C.String, C.String, C.Boolean, C.Long, C.Int,
              hookBefore { log("Recording", *args) })
        }
  }
}