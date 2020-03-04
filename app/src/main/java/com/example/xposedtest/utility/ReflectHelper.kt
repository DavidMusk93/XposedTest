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

  fun getFieldName(clz: Class<*>, type: Class<*>, sequence: Int): String? {
    var i = 0
    for (field in clz.declaredFields) {
      if (field.type == type && ++i == sequence) {
        return field.name
      }
    }
    return null
  }

  fun getMethodName(clz: Class<*>, returnType: Class<*>, vararg parameterTypes: Class<*>): String? {
    var i: Int
    val count = parameterTypes.size
    for (method in clz.declaredMethods) {
      if (method.parameterCount == count && method.returnType == returnType) {
        i = -1
        while (++i < count) {
          if (method.parameterTypes[i] != parameterTypes[i]) {
            break
          }
        }
        if (i == count) {
          return method.name
        }
      }
    }
    return null
  }

}

fun Class<*>.fieldName(type: Class<*>, sequence: Int = 1) = ReflectHelper.getFieldName(this, type, sequence)
fun Class<*>.methodName(returnType: Class<*>, vararg parameterTyeps: Class<*>) = ReflectHelper.getMethodName(this, returnType, *parameterTyeps)