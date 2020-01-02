package com.example.xposedtest.utility

import android.os.Bundle
import android.util.Log
import com.example.xposedtest.utility.CommonUtil.Companion.typeCast
import de.robv.android.xposed.XposedHelpers
import java.util.concurrent.atomic.AtomicInteger

class DebugUtil {
  companion object {
    fun log(msg: String?) = Log.i("@sun", "${Thread.currentThread().name}# $msg")

    fun showArgs(tag: String, args: Array<Any>, count: Int, action: (Any?, Int) -> String?) {
      val i = AtomicInteger(0)
      (0 until count).forEach {
        val arg = args[i.getAndIncrement()]
        val info = action(arg, it)
        log("$tag${i.get()}. $info")
      }
      log("$tag<<<")
    }

    fun flattenBundle(arg: Any?): String? {
      arg ?: return null
      val bundle = typeCast<Bundle>(arg)
      bundle ?: return null
      val sb = StringBuffer()
      for (key in bundle.keySet()) {
        sb.append("$key:${bundle.get(key)}, ")
      }
      return "{${sb.toString().removeSuffix(", ")}}"
    }

    fun flattenMap(map: Any?): String? {
      val sb = StringBuilder()
      typeCast<Map<String, String>>(map)
          ?.run {
            this.forEach { k, v -> sb.append("$k:$v, ") }
            return "{${sb.toString().removeSuffix(", ")}}"
          }
          ?: return null
    }

    fun <T> flattenList(list: Any?): String? {
      typeCast<List<T>>(list)
          ?.run { return this.joinToString() }
          ?: return null
    }

    inline fun <reified T> print_member(obj: Any?, name: String) {
      val type = "${T::class.java}".substringAfterLast('.')
      obj?.run {
        val prefix = "[showMember][$this:$name]"
        when (type) {
          "Long" -> log("$prefix${typeCast<T>(XposedHelpers.getLongField(this, name))}")
          "Integer" -> log("$prefix${typeCast<T>(XposedHelpers.getIntField(this, name))}")
          "Boolean" -> log("$prefix${typeCast<T>(XposedHelpers.getBooleanField(this, name))}")
          "String" -> log("$prefix${typeCast<T>(XposedHelpers.getObjectField(this, name))}")
          "Object" -> {
            val field = XposedHelpers.getObjectField(this, name)
            field?.run {
              if ("${this::class.java}".substringAfterLast('.').contains("brj")) {
                log("$prefix${typeCast<String>(XposedHelpers.getObjectField(this, "wiP"))}")
              }
            }
          }
          else -> log("${prefix}unknown type???")
        }
      }
    }

  }
}