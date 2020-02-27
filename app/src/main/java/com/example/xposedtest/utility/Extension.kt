package com.example.xposedtest.utility

import de.robv.android.xposed.XposedHelpers
import java.io.File
import java.io.IOException

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

@Throws(IOException::class)
fun String.toFile(path: String) {
  File(path).printWriter().apply {
    print(this@toFile)
    close()
  }
}

fun Any.getClassNameFromObject() = "$this".substringAfter(' ').substringBeforeLast('@')

inline fun <reified T> Any.getField(field: String): T? {
  return XposedHelpers.getObjectField(this, field).cast()
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

fun Any.callMethod(name: String, vararg args: Any?) = XposedHelpers.callMethod(this, name, *args)

fun Class<*>.callStaticMethod(name: String, vararg args: Any?) = XposedHelpers.callStaticMethod(this, name, *args)
