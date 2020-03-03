package com.example.xposedtest.utility

import java.lang.reflect.Field
import java.lang.reflect.Method

object ReflectHelper {

  fun getField(obj: Any, name: String): Field? {
    var clz: Class<*>? = obj::class.java
    while (clz != null) {
      kotlin.runCatching {
        obj::class.java.getDeclaredField(name).let {
          if (!it.isAccessible)
            it.isAccessible = true
          return it
        }
      }.onFailure { clz = clz?.superclass }
    }
    return null
  }

  fun getMethod(obj: Any, name: String, vararg parameterTyeps: Class<*>): Method? {
    var clz: Class<*>? = obj::class.java
    while (clz != null) {
      kotlin.runCatching {
        obj::class.java.getDeclaredMethod(name, *parameterTyeps).let {
          if (!it.isAccessible)
            it.isAccessible = true
          return it
        }
      }.onFailure { clz = clz?.superclass }
    }
    return null
  }

  fun getFieldName(clz: Class<*>, type: String, sequence: Int): String? {
    var i = 0
    for (field in clz.declaredFields) {
      if ("${field.type}".endsWith(type) && ++i == sequence) {
        return field.name
      }
    }
    return null
  }

}

fun Class<*>.fieldName(type: String, sequence: Int = 1) = ReflectHelper.getFieldName(this, type, sequence)