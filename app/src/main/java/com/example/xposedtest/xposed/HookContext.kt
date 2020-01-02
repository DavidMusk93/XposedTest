package com.example.xposedtest.xposed

import android.app.Application
import com.example.xposedtest.utility.C
import com.example.xposedtest.utility.cast
import de.robv.android.xposed.XposedHelpers
import java.lang.ref.WeakReference

open class HookContext(
    var ref: WeakReference<Application>? = null,
    var classLoader: ClassLoader? = null,
    var processName: String? = null
) : BaseHook {

  override fun attachBaseContext() {
    C.ContextWrapper.hook("attachBaseContext", C.Context,
        hookAfter { thisObject.cast<Application>()?.run { ref = WeakReference(this) } })
  }

  override fun onResume(tag: String) {
    C.Activity.hook("onResume", hookBefore { gLog(tag + "_onResume", thisObject) })
  }

  fun baseHook(tag: String) {
    attachBaseContext()
    onResume(tag)
  }

  fun findClass(className: String): Class<*>? = XposedHelpers.findClassIfExists(className, classLoader)
}