package com.example.xposedtest.module.tomato

import android.view.View
import java.lang.ref.WeakReference

class AdViewContext(var v1: WeakReference<View>? = null, var v2: WeakReference<View>? = null) {

  var count = 0

  fun testCount(): Boolean {
    if (++count == 2) {
      count = 0
      return true
    }
    return false
  }

  fun reset() {
    v1 = null
    v2 = null
  }

}