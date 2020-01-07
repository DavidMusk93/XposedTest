package com.example.xposedtest.xposed

import android.widget.Toast
import com.example.xposedtest.utility.DebugUtil
import de.robv.android.xposed.callbacks.XC_LoadPackage
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ToastContext(var start: Long, var job: Job? = null) {

  companion object {
    val MAX_GAP = 2000L
  }

  var disable = false

}

open class HookEntry(lpparam: XC_LoadPackage.LoadPackageParam, val context: HookContext) {

  private val toastContext = ToastContext(System.currentTimeMillis())

  init {
    context.classLoader = lpparam.classLoader
    context.processName = lpparam.processName
  }

  fun String.`class`(): Class<*>? {
    return context.findClass(this)
  }

  fun log(tag: String, vararg param: Any?) {
    gLog("@${context.processName}@$tag", *param)
  }

  fun setupHook(tag: String) {
    context.baseHook(tag)
    DebugUtil.log("@@@@@@ N E W  P R O C E S S @@@@@@")
    DebugUtil.log(context.processName)
    DebugUtil.log("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
  }

  fun String.toast() {
    val now = System.currentTimeMillis()
    toastContext.job?.cancel()
    if (now - toastContext.start > ToastContext.MAX_GAP) {
      context.ref?.get()?.let { Toast.makeText(it, this, Toast.LENGTH_SHORT).show() }
      toastContext.start = now
    } else {
      toastContext.job = MainScope().launch {
        delay(ToastContext.MAX_GAP)
        context.ref?.get()?.let { Toast.makeText(it, this@toast, Toast.LENGTH_SHORT).show() }
      }
    }
  }

  fun String.toast2() {
    val now = System.currentTimeMillis()
    toastContext.disable = toastContext.disable && (now - toastContext.start < ToastContext.MAX_GAP)
    if (!toastContext.disable) {
      toastContext.disable = true
      toastContext.start = now
      context.ref?.get()?.let { Toast.makeText(it, this, Toast.LENGTH_SHORT).show() }
    }
  }

}