package com.example.xposedtest.db

import de.robv.android.xposed.XposedHelpers
import java.util.concurrent.atomic.AtomicInteger

object DbHelper {
  fun parseCursor(cursor: Any, vararg methodSet: String): ArrayList<Any?> {
    val result = arrayListOf<Any?>()
    val i = AtomicInteger(0)
    for (m in methodSet) {
      result.add(XposedHelpers.callMethod(cursor, m, i.getAndIncrement()))
    }
    return result
  }
}