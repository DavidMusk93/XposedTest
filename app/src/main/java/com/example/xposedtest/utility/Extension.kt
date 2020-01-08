package com.example.xposedtest.utility

import android.app.Application
import android.widget.Toast
import com.example.xposedtest.MainHook
import de.robv.android.xposed.XposedHelpers
import java.io.File
import java.io.IOException
import java.lang.ref.WeakReference

inline fun <reified T> Any?.cast(): T? {
  if (this is T)
    return this
  return null
}

fun String.basename(): String? {
  val i = this.lastIndexOf('/')
  if (i != -1) {
    return this.substring(i + 1)
  }
  return null
}

var lastToastTime: Long = -1
fun String.toast(ref: WeakReference<Application>? = MainHook.gMiuiSettingsRef) {
  val timestamp = System.currentTimeMillis()
  // Avoid over-toast
  if (timestamp - lastToastTime < 2000L)
    return
  lastToastTime = timestamp
  ref?.get()?.let { Toast.makeText(it, this, Toast.LENGTH_SHORT).show() }
}

@Throws(IOException::class)
fun String.toFile(path: String) {
  File(path).printWriter().apply {
    print(this@toFile)
    close()
  }
}

inline fun <reified T> Any.getField(field: String): T? {
  return XposedHelpers.getObjectField(this, field) as T
}

inline fun <reified T> Any.setField(field: String, t: T) {
  XposedHelpers.setObjectField(this, field, t)
}

inline fun <reified T> Class<*>.getStaticField(field: String): T? {
  return XposedHelpers.getStaticObjectField(this, field).cast()
}

inline fun <reified T> Class<*>.setStaticField(field: String, t: T) {
  XposedHelpers.setStaticObjectField(this, field, t)
}